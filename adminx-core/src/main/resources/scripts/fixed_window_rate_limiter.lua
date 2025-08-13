-- 固定窗口限流器
-- 返回 允许-true 拒绝-false
local current = redis.call('GET', KEYS[1])

-- 检查是否超限
if current and tonumber(current) >= tonumber(ARGV[1]) then
    return false
end

-- 增加计数
redis.call('INCR', KEYS[1])

-- 首次请求时设置过期时间
if current == false then
    redis.call('EXPIRE', KEYS[1], ARGV[2])
end

return true