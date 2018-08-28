/*
 * Copyright (c) 2017 Cisco Systems Inc and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.unimgr.mef.nrp.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nonnull;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.unimgr.mef.nrp.api.RequestValidator;
import org.opendaylight.unimgr.mef.nrp.common.NrpDao;
import org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.common.rev180307.LocalClass;
import org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.connectivity.rev180307.CreateConnectivityServiceInput;
import org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.connectivity.rev180307.UpdateConnectivityServiceInput;
import org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.connectivity.rev180307.connectivity.context.ConnectivityService;
import org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.connectivity.rev180307.update.connectivity.service.input.EndPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author bartosz.michalik@amartus.com
 */
public class DefaultValidator implements RequestValidator {

    private static final Logger log = LoggerFactory.getLogger(DefaultValidator.class);

    private final  DataBroker dataBroker;

    public DefaultValidator(DataBroker dataBroker) {
        this.dataBroker = dataBroker;
    }

    @Override
    public ValidationResult checkValid(CreateConnectivityServiceInput input) {
        log.debug("Validation for requrst started");
        ValidationResult fromInput= verifyPayloadCorrect(input);

        ValidationResult fromState = validateState(input);



        return fromState.merge(fromInput);
    }

    @Nonnull protected  ValidationResult validateState(CreateConnectivityServiceInput input) {
        // simple usecase to validate port based service
        // more complex implementation could use caching techniques
        //TODO implement

        return new ValidationResult();
    }

    @Nonnull protected ValidationResult verifyPayloadCorrect(CreateConnectivityServiceInput input) {

        ValidationResult validationResult = new ValidationResult();

        if(input.getEndPoint() == null || input.getEndPoint().isEmpty()) {
            validationResult.problem("No endpoints specified for a connectivity service");
        } else {

            Set<String> allItems = new HashSet<>();
            Optional<String> firstDuplicate = input.getEndPoint().stream()
                    .filter(e -> e.getLocalId() != null)
                    .map(LocalClass::getLocalId)
                    .filter(s -> !allItems.add(s))
                    .findFirst();
            firstDuplicate.ifPresent(s -> validationResult.problem("A duplicate endpoint id: " + s));
        }



        return validationResult;
    }

    @Override
    public ValidationResult checkValid(UpdateConnectivityServiceInput input) {
        ConnectivityService cs = new NrpDao(dataBroker.newReadOnlyTransaction()).getConnectivityService(input.getServiceIdOrName());

        if(cs == null) {
            return new ValidationResult().problem(String.format("Connectivity service %s does not exists", input.getServiceIdOrName()));
        }

        EndPoint ep = input.getEndPoint();
        if(ep == null) return new ValidationResult().problem(String.format("Endpoint not defined for %s", input.getServiceIdOrName()));

        Optional<org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.connectivity.rev180307.connectivity.service.EndPoint> epFromCs = cs.getEndPoint().stream()
                .filter(e -> e.getLocalId().equals(ep.getLocalId())).findFirst();
        if(! epFromCs.isPresent())
            return new ValidationResult().problem(String.format("No endpoint with local id %1$s defined for %2$s", ep.getLocalId(), input.getServiceIdOrName()));

        if(!epFromCs.get().getServiceInterfacePoint().equals(ep.getServiceInterfacePoint())) {
            return new ValidationResult().problem(String.format("Sip mapping for endpoint %1$s is not matching for service %2$s", ep.getLocalId(), input.getServiceIdOrName()));
        }

        return new ValidationResult();
    }

    @Override
    public ValidationResult checkValidServiceInterfacePoint(CreateConnectivityServiceInput input) {
        boolean isExclusive = input.getConnConstraint().isIsExclusive();
        List<org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.connectivity.rev180307.create.connectivity.service.input.EndPoint> inputEndPointList =
                input.getEndPoint();
        List<ConnectivityService> serviceList =
                new NrpDao(dataBroker.newReadOnlyTransaction()).getConnectivityServiceList();

        if (serviceList == null) {
            return new ValidationResult();
        } else {
            for (ConnectivityService service : serviceList) {
                List<org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.connectivity.rev180307.connectivity.service.EndPoint> serviceEndPointList =
                        service.getEndPoint();
                for (org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.connectivity.rev180307.create.connectivity.service.input.EndPoint inputServiceEndpoint : inputEndPointList) {

                    for (org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.connectivity.rev180307.connectivity.service.EndPoint servicEndPoint : serviceEndPointList) {

                        if (areEndpointsEqual(servicEndPoint, inputServiceEndpoint)) {
                            if (isExclusive == true
                                    || (isExclusive == false && service.isIsExclusive() == true)) {
                                return new ValidationResult().problem(
                                        String.format("Endpoint %s already in use by service %s",
                                                inputServiceEndpoint.getServiceInterfacePoint()
                                                        .getServiceInterfacePointId().getValue(),
                                                service.getUuid().getValue()));
                            }
                        }

                    }
                }
            }

        }
        return new ValidationResult();
    }
    
    private boolean areEndpointsEqual(
            org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.connectivity.rev180307.connectivity.service.EndPoint servicEndPoint,
            org.opendaylight.yang.gen.v1.urn.onf.otcc.yang.tapi.connectivity.rev180307.create.connectivity.service.input.EndPoint inputServiceEndpoint) {
        return servicEndPoint.getServiceInterfacePoint().getServiceInterfacePointId().getValue()
                .equalsIgnoreCase(inputServiceEndpoint.getServiceInterfacePoint()
                        .getServiceInterfacePointId().getValue());
   }
    
}
