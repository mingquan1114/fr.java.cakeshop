package com.fr.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class TranslationUtil {

    private final MessageSource messageSource;

    public TranslationUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String translate(String key) {
        if (key == null || key.isEmpty()) {
            return key;
        }
        
        String translationKey = "db." + key;
        try {
            String translated = messageSource.getMessage(translationKey, null, key, LocaleContextHolder.getLocale());
            return translated;
        } catch (Exception e) {
            return key;
        }
    }

    public String translateCategory(String categoryName) {
        if (categoryName == null || categoryName.isEmpty()) {
            return categoryName;
        }
        
        String translationKey = "db.category." + categoryName;
        try {
            String translated = messageSource.getMessage(translationKey, null, categoryName, LocaleContextHolder.getLocale());
            return translated;
        } catch (Exception e) {
            return categoryName;
        }
    }

    public String translateRole(String roleName) {
        if (roleName == null || roleName.isEmpty()) {
            return roleName;
        }
        
        String translationKey = "db.role." + roleName;
        try {
            String translated = messageSource.getMessage(translationKey, null, roleName, LocaleContextHolder.getLocale());
            return translated;
        } catch (Exception e) {
            return roleName;
        }
    }

    public String translateOrderStatus(String status) {
        if (status == null || status.isEmpty()) {
            return status;
        }
        
        String translationKey = "db.order.status." + status;
        try {
            String translated = messageSource.getMessage(translationKey, null, status, LocaleContextHolder.getLocale());
            return translated;
        } catch (Exception e) {
            return status;
        }
    }

    public String translateUserStatus(String status) {
        if (status == null || status.isEmpty()) {
            return status;
        }
        
        String translationKey = "db.user.status." + status;
        try {
            String translated = messageSource.getMessage(translationKey, null, status, LocaleContextHolder.getLocale());
            return translated;
        } catch (Exception e) {
            return status;
        }
    }

    public String translateGoods(String goodsName) {
        if (goodsName == null || goodsName.isEmpty()) {
            return goodsName;
        }
        
        String translationKey = "db.goods." + goodsName;
        try {
            String translated = messageSource.getMessage(translationKey, null, goodsName, LocaleContextHolder.getLocale());
            return translated;
        } catch (Exception e) {
            return goodsName;
        }
    }

    public String translateUserName(String userName) {
        if (userName == null || userName.isEmpty()) {
            return userName;
        }
        
        String translationKey = "db.user.name." + userName;
        try {
            String translated = messageSource.getMessage(translationKey, null, userName, LocaleContextHolder.getLocale());
            return translated;
        } catch (Exception e) {
            return userName;
        }
    }

    public String translateAddress(String address) {
        if (address == null || address.isEmpty()) {
            return address;
        }
        
        String translationKey = "db.address." + address;
        try {
            String translated = messageSource.getMessage(translationKey, null, address, LocaleContextHolder.getLocale());
            return translated;
        } catch (Exception e) {
            return address;
        }
    }
}