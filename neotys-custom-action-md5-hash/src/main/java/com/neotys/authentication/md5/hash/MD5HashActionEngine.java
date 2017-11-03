package com.neotys.authentication.md5.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.SampleResult;

public final class MD5HashActionEngine implements ActionEngine {

	private static String message; 			//Message to hash
	
	private static String MD5Hash(String message) {
	//Hashes a messages through MD5
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] array = md.digest(message.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.
						toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
			}	
			
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	@Override
	public SampleResult execute(Context context, List<ActionParameter> parameters) {
		final SampleResult sampleResult = new SampleResult();
		final StringBuilder requestBuilder = new StringBuilder();
		final StringBuilder responseBuilder = new StringBuilder();

		//List and initialize all the parameters
		for (ActionParameter temp:parameters) {
			appendLineToStringBuilder(requestBuilder,
					String.format("%s: %s",temp.getName(),temp.getValue()));
			switch (temp.getName().toLowerCase()) {
			case "message":							//If the current parameter is a message 
				message = temp.getValue();			//Initialize the message value
				break;			
			default :
				break;
			}
		}
				
		sampleResult.sampleStart();

		//Hash the message
		String hashedMessage = MD5Hash(message);
							

		sampleResult.sampleEnd();
		
		appendLineToStringBuilder(responseBuilder,
				String.format("Hashed message: %s", hashedMessage));


		sampleResult.setRequestContent(requestBuilder.toString());
		sampleResult.setResponseContent(responseBuilder.toString());
		return sampleResult;
	}

	private void appendLineToStringBuilder(final StringBuilder sb, final String line){
		sb.append(line).append("\n");
	}

	/**
	 * This method allows to easily create an error result and log exception.
	 */
	@SuppressWarnings("unused")
	private static SampleResult getErrorResult(final Context context, final SampleResult result, final String errorMessage, final Exception exception) {
		result.setError(true);
		result.setStatusCode("NL-MD5Hash_ERROR");
		result.setResponseContent(errorMessage);
		if(exception != null){
			context.getLogger().error(errorMessage, exception);
		} else{
			context.getLogger().error(errorMessage);
		}
		return result;
	}

	@Override
	public void stopExecute() {
		// TODO add code executed when the test have to stop.
	}

}
