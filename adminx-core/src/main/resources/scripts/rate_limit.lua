local key = KEYS[1]
local max = tonumber(ARGV[1])
local window = tonumber(ARGV[2])

-- 计数
local count = redis.call('INCR', key)

-- 首次请求时设置过期时间
if count == 1 then
    redis.call('EXPIRE', key, window)
end

-- 是否超过限制
if count > max then
    return -1
end

-- 返回当前计数
return count