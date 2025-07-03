package com.soybean.event;

import com.soybean.event.impl.*;

public class EventRegister {
    public static void Initialize(){
        EventBreak.register();
        EventUseEntity.register();
        EventUseItem.register();
        EventTick.register();
        EventUseBlock.register();
        EventItemFuel.register();
    }

    public static void InitializeClient(){
        EventTick.registerClient();
        EventHud.init();
    }
}
