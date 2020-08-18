local low = tonumber(ARGV[1])
local high = tonumber(ARGV[2])
local data = redis.call('lrange', KEYS[1], ARGV[1],ARGV[2])
local size = table.getn(data);
if size == 0 then
    return nil
else
    redis.call('ltrim', KEYS[1], size,-1)
    return cjson.encode(data)
end