package com.soybean.utils;

/**
 * @author soybean
 * @date 2025/03/25
 * @description 动态光源接口，可以被物品实现以提供自定义光照等级
 */
public interface DynamicLightSource {
    /**
     * 获取物品的光照等级
     * @return 光照等级 (0-15)
     */
    int getLightLevel();
}
