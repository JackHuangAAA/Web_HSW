(function(window){
	window.$d = {
		_device_ready: function(){
			console.log('_device_ready')
			window.$d.invoke = function(pluginName,functionName,data){
				return new Promise(function(resolve,reject){
					bridge.Call(JSON.stringify({
						PLUGIN: pluginName,
						FUNCTION: functionName,
						DATA: data
					}),function(e,d){
						if(e)
							reject(e)
						else
							resolve(JSON.parse(d))
					})
				});
			};
			window.$d.subscribe = function(eventName,cb){
				bridge.Register(eventName,function(d){
					console.log(eventName + ' callback')
					cb(JSON.parse(d))
				})
			};
			window.$d.log = function(msg){
				bridge.WriteLog("info",msg)
			};
			window.$d.getPluginList = function(){
				return new Promise(function(resolve,reject){
					bridge.GetPluginList(function(e,d){
						if(e)
							reject(e)
						else
							resolve(JSON.parse(d))
					})
				});
			};
			window.$d.getPluginEventList = function(pluginName){
				return new Promise(function(resolve,reject){
					bridge.GetPluginEventList(pluginName, function(e,d){
						if(e)
							reject(e)
						else
							resolve(JSON.parse(d))
					})
				});
			};
			window.$d.getPluginFunctionList = function(pluginName){
				return new Promise(function(resolve,reject){
					bridge.GetPluginFunctionList(pluginName, function(e,d){
						if(e)
							reject(e)
						else
							resolve(JSON.parse(d))
					})
				});
			}
			window.$d.onReady();
		},
		ready: function(){
			console.log("$d ready");
		},
		invoke: function(pluginName,functionName,data){
			console.log("$d.call"+pluginName+functionName+data)
			return Promise.reject("not support");
		},
		subscribe: function(eventName,cb){
			console.log("$d.subscribe"+eventName)
		},
		log: function(msg){
			console.log("$d.log"+msg)
		},
		getPluginList: function(){
			console.log("$d.getPluginList")
			return Promise.reject("not support");
		},
		getPluginEventList: function(pluginName){
			console.log("$d.getPluginEventList"+pluginName)
			return Promise.reject("not support");
		},
		getPluginFunctionList: function(pluginName){
			console.log("$d.getPluginFunctionList"+pluginName)
			return Promise.reject("not support");
		}
	};
})(window);