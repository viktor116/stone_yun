package com.soybean.event;

import com.soybean.event.impl.EventBreak;
import com.soybean.event.impl.EventTick;
import com.soybean.event.impl.EventUseEntity;
import com.soybean.event.impl.EventUseItem;

public class EventRegister {
    public static void Initialize(){
        EventBreak.register();
        EventUseEntity.register();
        EventUseItem.register();
        EventTick.register();
    }
}
