local count = redis.call('GET', KEYS[1])
-- 如果已超过限制，直接返回false
if count and tonumber(count) >= tonumber(ARGV[1]) then
    return false
end
-- 增加计数
redis.call('INCR', KEYS[1])
-- 如果是首次设置，添加过期时间
if count == false then
    redis.call('EXPIRE', KEYS[1], ARGV[2])
end
return true