package io.github.dgrandino.travelsjavaapi.factory.impl;

import io.github.dgrandino.travelsjavaapi.factory.TravelFactory;
import io.github.dgrandino.travelsjavaapi.model.Travel;
import io.github.dgrandino.travelsjavaapi.model.TravelTypeEnum;
import org.springframework.boot.BootstrapRegistryInitializer;

public class TravelFactoryImpl implements TravelFactory {
    @Override
    public Travel createTravel(String type){
        if(TravelTypeEnum.ONE_WAY.getValue().equals(type)){
            return new Travel(TravelTypeEnum.ONE_WAY);
        }
        else if(TravelTypeEnum.MULTI_CITY.getValue().equals(type)){
            return new Travel(TravelTypeEnum.MULTI_CITY);
        }

        return new Travel(TravelTypeEnum.RETURN);
    }
}
