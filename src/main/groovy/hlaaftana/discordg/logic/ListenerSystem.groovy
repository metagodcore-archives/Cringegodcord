package hlaaftana.discordg.logic

import java.util.Map;

import groovy.lang.Closure
import hlaaftana.discordg.util.Log

abstract class ListenerSystem {
	Map listeners = [:]

	abstract parseEvent(param)

	abstract listenerError(event, Throwable ex, Closure closure, data)

	def submit(event, boolean temporary = false, Closure closure){
		addListener(event, temporary, closure)
	}

	def addListener(event, boolean temporary = false, Closure closure) {
		event = parseEvent(event)
		Closure ass = closure.clone()
		if (temporary) ass = { Map d -> ass(d); listeners[event].remove(ass) }
		if (listeners.containsKey(event)) listeners[event] += ass
		else listeners[event] = [ass]
		ass
	}
	
	Closure listen(event, boolean temporary = false, Closure closure){
		Closure ass
		ass = { Map d, Closure internal ->
			//d["rawClosure"] = closure
			//d["closure"] = ass
			Closure copy = closure.clone()
			copy.delegate = d
			copy.parameterTypes.size() == 2 ? copy(copy.delegate, internal) : copy(copy.delegate)
		}
		addListener(event, temporary, ass)
	}

	def removeListener(event, Closure closure) {
		listeners[parseEvent(event)]?.remove(closure)
	}

	def removeListenersFor(event){
		listeners.remove(parseEvent(event))
	}

	def removeAllListeners(){
		listeners = [:]
	}

	def dispatchEvent(type, data){
		for (l in listeners[parseEvent(type)]){
			def a = l.clone()
			try{
				if (a.parameterTypes.length > 1) a(data, l)
				else a(data)
			}catch (ex){
				listenerError(parseEvent(type), ex, l, data)
			}
		}
	}
}
