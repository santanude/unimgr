package org.opendaylight.unimgr.utils;

import java.util.concurrent.atomic.AtomicLong;
import org.opendaylight.unimgr.mef.nrp.cisco.xr.l2vpn.helper.PseudowireHelper.IdGenerator;

public class Test {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        System.out.print("=====" + IdGenerator.generate());
        System.out.print("=====" + IdGenerator.generate());
        System.out.print("=====" + IdGenerator.generate());
        
       
    }
    
    

    public static class IdGenerator {
        private static final AtomicLong idGenerator = new AtomicLong(2000L);

        public static long generate() {
            //TODO implement real pseudowire-id generator
            return idGenerator.getAndIncrement();
        }
    }


}
