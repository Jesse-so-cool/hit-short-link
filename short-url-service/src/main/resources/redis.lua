local low = tonumber(ARGV[1]);
local high = tonumber(ARGV[2]);

--local data = redis.call('lrange', KEYS[1],low,high)
local res = cjson.encode({'1','2'})
return res
