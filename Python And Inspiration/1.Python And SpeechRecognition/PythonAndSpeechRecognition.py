import time;
import winsound;
import speech_recognition as SpeechRecognition;

def write(fileName, text):
	file=open(fileName,'w');
	file.write(text);
	file.close();

def beep():
	frequency = 2500; duration = 500;
	winsound.Beep(frequency, duration);

def callback(recognizer, audio):
	print("Google Speech Recognition:");
	try:
		print("Google Speech Recognition thinks you said " + recognizer.recognize_google(audio));
	except SpeechRecognition.UnknownValueError:
		print("Google Speech Recognition could not understand audio");
	except SpeechRecognition.RequestError as error:
		print("Could not request results from Google Speech Recognition service; {0}".format(error));

recognizer=SpeechRecognition.Recognizer();
def testSpeechRecognitionInBackground(delay):
	microphone=SpeechRecognition.Microphone();
	with microphone as source:
		recognizer.adjust_for_ambient_noise(source);
	isListening=recognizer.listen_in_background(microphone, callback);
	time.sleep(delay);
	isListening(False);

def testSpeechRecognitionWithAudioFile(fileName):
	with SpeechRecognition.AudioFile(fileName) as audioFile:
		audio=recognizer.record(audioFile);
		try:
			text=recognizer.recognize_google(audio);
			print("Audio File said: "+text);
		except SpeechRecognition.UnknownValueError:
			print("Google Speech Recognition could not understand audio");
		except SpeechRecognition.WaitTimeoutError:
			print("Google Speech Recognition could not hear anything");
		except SpeechRecognition.RequestError as error:
			print("Could not request results from Google Speech Recognition service; {0}".format(error));

def testSpeechRecognitionInForeground(timeout):
	with SpeechRecognition.Microphone() as microphone:
		print("A moment of silence");
		recognizer.adjust_for_ambient_noise(microphone, duration=2);
		print("Say something!"); beep();
		try:
			audio=recognizer.listen(microphone, timeout);
			print("Trying to recognize audio");
			text=recognizer.recognize_google(audio);
			print("You just said: "+text); 
			write("audio.txt", text);
			if(text.find("exit")!=-1): return 0;
		except SpeechRecognition.UnknownValueError:
			print("Google Speech Recognition could not understand audio");
		except SpeechRecognition.WaitTimeoutError:
			print("Google Speech Recognition could not hear anything");
		except SpeechRecognition.RequestError as error:
			print("Could not request results from Google Speech Recognition service; {0}".format(error));
	return 1;

isRunning=1;
while(isRunning==1): isRunning=testSpeechRecognitionInForeground(2);
