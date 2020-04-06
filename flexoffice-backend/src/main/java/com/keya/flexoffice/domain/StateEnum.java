package com.keya.flexoffice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public  enum StateEnum {
    FREE{
        @Override
        StateEnum nextState(StateEnum nextVal) {
            switch (nextVal){
                case OCCUPIED:
                    return OCCUPIED;
                    default:
                        return null; }
        }


    },
    OCCUPIED{
        @Override
        StateEnum nextState(StateEnum nextVal) {
            switch (nextVal){
                case FREE:
            return FREE;
                default:
                    return null;
        }
    }

};



     abstract StateEnum nextState(StateEnum currentStateEnum);


}
//</editor-fold>
