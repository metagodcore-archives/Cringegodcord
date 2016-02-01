package ml.hlaaftana.discordg.util

import java.text.SimpleDateFormat
import java.util.Date;

class ConversionUtil {
	static String encodeToBase64(File image){
		return "data:image/jpg;base64," + image.bytes.encodeBase64().toString()
	}

	static Date toDiscordDate(String string){
		return new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSSSSXXX").parse(string)
	}
}