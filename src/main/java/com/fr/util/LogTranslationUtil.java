package com.fr.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LogTranslationUtil {

    private static final Map<String, Map<String, String>> operationTranslations = new HashMap<>();
    private static final Map<String, Map<String, String>> moduleTranslations = new HashMap<>();
    private static final Map<String, Map<String, String>> roleTranslations = new HashMap<>();

    static {
        Map<String, String> zhOperations = new HashMap<>();
        zhOperations.put("登录", "登录");
        zhOperations.put("退出", "退出");
        zhOperations.put("接单", "接单");
        zhOperations.put("取货", "取货");
        zhOperations.put("送达", "送达");
        zhOperations.put("修改资料", "修改资料");
        zhOperations.put("修改密码", "修改密码");
        zhOperations.put("修改用户", "修改用户");
        zhOperations.put("审核用户", "审核用户");
        zhOperations.put("设置管理员", "设置管理员");
        zhOperations.put("删除用户", "删除用户");
        zhOperations.put("添加商品", "添加商品");
        zhOperations.put("修改商品", "修改商品");
        zhOperations.put("删除商品", "删除商品");
        zhOperations.put("订单发货", "订单发货");
        zhOperations.put("完成订单", "完成订单");
        zhOperations.put("添加分类", "添加分类");
        zhOperations.put("删除分类", "删除分类");
        zhOperations.put("添加购物车", "添加购物车");
        zhOperations.put("删除购物车", "删除购物车");
        zhOperations.put("取消订单", "取消订单");
        zhOperations.put("创建订单", "创建订单");
        operationTranslations.put("zh", zhOperations);

        Map<String, String> enOperations = new HashMap<>();
        enOperations.put("登录", "Login");
        enOperations.put("退出", "Logout");
        enOperations.put("接单", "Accept Order");
        enOperations.put("取货", "Pickup");
        enOperations.put("送达", "Deliver");
        enOperations.put("修改资料", "Update Profile");
        enOperations.put("修改密码", "Change Password");
        enOperations.put("修改用户", "Update User");
        enOperations.put("审核用户", "Validate User");
        enOperations.put("设置管理员", "Set Admin");
        enOperations.put("删除用户", "Delete User");
        enOperations.put("添加商品", "Add Goods");
        enOperations.put("修改商品", "Update Goods");
        enOperations.put("删除商品", "Delete Goods");
        enOperations.put("订单发货", "Ship Order");
        enOperations.put("完成订单", "Complete Order");
        enOperations.put("添加分类", "Add Category");
        enOperations.put("删除分类", "Delete Category");
        enOperations.put("添加购物车", "Add to Cart");
        enOperations.put("删除购物车", "Remove from Cart");
        enOperations.put("取消订单", "Cancel Order");
        enOperations.put("创建订单", "Create Order");
        operationTranslations.put("en", enOperations);

        Map<String, String> zhModules = new HashMap<>();
        zhModules.put("骑士端", "骑士端");
        zhModules.put("系统管理", "系统管理");
        zhModules.put("用户管理", "用户管理");
        zhModules.put("商品管理", "商品管理");
        zhModules.put("订单管理", "订单管理");
        zhModules.put("分类管理", "分类管理");
        zhModules.put("购物车", "购物车");
        zhModules.put("订单", "订单");
        moduleTranslations.put("zh", zhModules);

        Map<String, String> enModules = new HashMap<>();
        enModules.put("骑士端", "Knight");
        enModules.put("系统管理", "System Management");
        enModules.put("用户管理", "User Management");
        enModules.put("商品管理", "Goods Management");
        enModules.put("订单管理", "Order Management");
        enModules.put("分类管理", "Category Management");
        enModules.put("购物车", "Cart");
        enModules.put("订单", "Order");
        moduleTranslations.put("en", enModules);

        Map<String, String> zhRoles = new HashMap<>();
        zhRoles.put("管理员", "管理员");
        zhRoles.put("骑手", "骑手");
        zhRoles.put("普通用户", "普通用户");
        roleTranslations.put("zh", zhRoles);

        Map<String, String> enRoles = new HashMap<>();
        enRoles.put("管理员", "Admin");
        enRoles.put("骑手", "Rider");
        enRoles.put("普通用户", "User");
        roleTranslations.put("en", enRoles);

        Map<String, String> jaOperations = new HashMap<>();
        jaOperations.put("登录", "ログイン");
        jaOperations.put("退出", "ログアウト");
        jaOperations.put("接单", "注文を受け取る");
        jaOperations.put("取货", "商品を受け取る");
        jaOperations.put("送达", "配達完了");
        jaOperations.put("修改资料", "プロフィール更新");
        jaOperations.put("修改密码", "パスワード変更");
        jaOperations.put("修改用户", "ユーザー更新");
        jaOperations.put("审核用户", "ユーザー確認");
        jaOperations.put("设置管理员", "管理者設定");
        jaOperations.put("删除用户", "ユーザー削除");
        jaOperations.put("添加商品", "商品追加");
        jaOperations.put("修改商品", "商品更新");
        jaOperations.put("删除商品", "商品削除");
        jaOperations.put("订单发货", "注文発送");
        jaOperations.put("完成订单", "注文完了");
        jaOperations.put("添加分类", "カテゴリー追加");
        jaOperations.put("删除分类", "カテゴリー削除");
        jaOperations.put("添加购物车", "カートに追加");
        jaOperations.put("删除购物车", "カートから削除");
        jaOperations.put("取消订单", "注文キャンセル");
        jaOperations.put("创建订单", "注文作成");
        operationTranslations.put("ja", jaOperations);

        Map<String, String> jaModules = new HashMap<>();
        jaModules.put("骑士端", "ライダー");
        jaModules.put("系统管理", "システム管理");
        jaModules.put("用户管理", "ユーザー管理");
        jaModules.put("商品管理", "商品管理");
        jaModules.put("订单管理", "注文管理");
        jaModules.put("分类管理", "カテゴリー管理");
        jaModules.put("购物车", "カート");
        jaModules.put("订单", "注文");
        moduleTranslations.put("ja", jaModules);

        Map<String, String> jaRoles = new HashMap<>();
        jaRoles.put("管理员", "管理者");
        jaRoles.put("骑手", "ライダー");
        jaRoles.put("普通用户", "ユーザー");
        roleTranslations.put("ja", jaRoles);
    }

    public static String translateOperation(String operation, Locale locale) {
        String lang = locale.getLanguage();
        Map<String, String> translations = operationTranslations.getOrDefault(lang, operationTranslations.get("zh"));
        return translations.getOrDefault(operation, operation);
    }

    public static String translateModule(String module, Locale locale) {
        String lang = locale.getLanguage();
        Map<String, String> translations = moduleTranslations.getOrDefault(lang, moduleTranslations.get("zh"));
        return translations.getOrDefault(module, module);
    }

    public static String translateRole(String role, Locale locale) {
        String lang = locale.getLanguage();
        Map<String, String> translations = roleTranslations.getOrDefault(lang, roleTranslations.get("zh"));
        return translations.getOrDefault(role, role);
    }
}