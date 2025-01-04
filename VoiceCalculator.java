import com.google.cloud.speech.v1.*;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.protobuf.ByteString;
import javax.sound.sampled.*;
import java.io.IOException;
import java.util.List;

public class VoiceCalculator {

    public static void main(String[] args) {
        try {
            // Record audio from the microphone
            ByteString audioData = recordAudio();

            // Use Google Cloud Speech API to transcribe the audio to text
            String recognizedText = recognizeSpeech(audioData);

            // Calculate the result based on the recognized text
            double result = evaluateExpression(recognizedText);

            // Output the result
            System.out.println("Result: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ByteString recordAudio() throws LineUnavailableException, IOException {
        // Set up the microphone for audio capture
        TargetDataLine microphone;
        AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        microphone = (TargetDataLine) AudioSystem.getLine(info);
        microphone.open(format);
        microphone.start();

        System.out.println("Recording audio... Speak now!");

        byte[] buffer = new byte[4096];
        ByteString.Builder audioData = ByteString.newBuilder();

        // Record for 5 seconds
        long endTime = System.currentTimeMillis() + 5000;
        while (System.currentTimeMillis() < endTime) {
            int bytesRead = microphone.read(buffer, 0, buffer.length);
            if (bytesRead > 0) {
                audioData.addAllBytes(ByteString.copyFrom(buffer, 0, bytesRead));
            }
        }

        microphone.stop();
        microphone.close();
        System.out.println("Recording stopped.");

        return audioData.build();
    }

    private static String recognizeSpeech(ByteString audioData) throws IOException {
        // Initialize the speech client
        try (SpeechClient speechClient = SpeechClient.create()) {
            // Configure the audio and recognition settings
            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                    .setSampleRateHertz(16000)
                    .setLanguageCode("en-US")
                    .build();
            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setContent(audioData)
                    .build();

            // Perform speech recognition
            RecognizeResponse response = speechClient.recognize(config, audio);
            List<SpeechRecognitionResult> results = response.getResultsList();

            if (results.isEmpty()) {
                return "";
            }

            SpeechRecognitionResult result = results.get(0);
            SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
            return alternative.getTranscript();
        }
    }

    private static double evaluateExpression(String expression) {
        // Simple evaluation of arithmetic expressions (e.g., "2 plus 2")
        expression = expression.toLowerCase();

        // Replace common words with symbols
        expression = expression.replace("plus", "+")
                               .replace("minus", "-")
                               .replace("times", "*")
                               .replace("divided by", "/");

        // Simple handling for invalid expressions
        try {
            return evaluateArithmeticExpression(expression);
        } catch (Exception e) {
            System.out.println("Invalid expression.");
            return Double.NaN;
        }
    }

    private static double evaluateArithmeticExpression(String expression) {
        // Very simple evaluation of arithmetic expressions using Java's eval (use with caution!)
        // For safety, consider using a safer arithmetic expression parser.
        return new ScriptEngineManager().getEngineByName("JavaScript").eval(expression);
    }
}
