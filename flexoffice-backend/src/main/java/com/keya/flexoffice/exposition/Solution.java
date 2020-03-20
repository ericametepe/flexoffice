package com.keya.flexoffice.exposition;

import com.keya.flexoffice.domain.StateEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Solution {
        @Data
        class Change{
                 long coin2 = 0;
                 long bill5 = 0;
                 long bill10= 0;




        }

            Change opt(long s){
                boolean test = StateEnum.FREE.equals(StateEnum.OCCUPIED);
                  Change  change = new Change();
                  long reste= s;

                  long rcoin2 = 0;
                  long rbill5 = 0;
                  long rbill10= 0;

                 if (reste<2){
                         return null;
                 }

                  rbill10=reste/10;
                  reste = reste%10;

                  rbill5=reste/5;
                  reste  = reste%5;

                 rcoin2=reste/2;
                 reste  = reste %2;


                  change.setCoin2(rcoin2);
                  change.setBill5(rbill5);
                  change.setBill10(rbill10);
                 return change;
          }

        public static void main(String args[]) {
                Solution solution = new Solution();
                Change change = solution.opt(14988989898989L);
                log.info(" Result {}",change);

                System.out.print("" +change);

        }


}



