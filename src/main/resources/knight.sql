-- 骑士端数据库脚本
-- 创建骑士表
CREATE TABLE IF NOT EXISTS `knight` (
    `id` VARCHAR(50) NOT NULL PRIMARY KEY COMMENT '骑士ID',
    `username` VARCHAR(50) NOT NULL COMMENT '账号',
    `password` VARCHAR(100) NOT NULL COMMENT '密码（加密）',
    `name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `phone` VARCHAR(20) NOT NULL COMMENT '手机号',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `status` INT DEFAULT 1 COMMENT '状态（1-在线，0-离线）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='骑士表';

-- 插入测试骑士账号（密码: 123456）
INSERT INTO `knight` (`id`, `username`, `password`, `name`, `phone`, `status`) VALUES
('K001', 'knight1', '123456', '张三', '13800138001', 1),
('K002', 'knight2', '123456', '李四', '13800138002', 1);

-- 订单表扩展字段（如果表已存在则使用ALTER TABLE）
ALTER TABLE `order` 
    ADD COLUMN IF NOT EXISTS `knight_id` VARCHAR(50) DEFAULT NULL COMMENT '配送骑士ID',
    ADD COLUMN IF NOT EXISTS `knight_name` VARCHAR(50) DEFAULT NULL COMMENT '骑士姓名',
    ADD COLUMN IF NOT EXISTS `knight_phone` VARCHAR(20) DEFAULT NULL COMMENT '骑士电话',
    ADD COLUMN IF NOT EXISTS `pickup_time` DATETIME DEFAULT NULL COMMENT '取货时间',
    ADD COLUMN IF NOT EXISTS `delivery_time` DATETIME DEFAULT NULL COMMENT '送达时间';