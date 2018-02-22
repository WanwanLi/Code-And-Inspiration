import time;
import winsound;
import win32com.client as win32;
import speech_recognition as SpeechRecognition;

def write(fileName, text):
	file=open(fileName,'w');
	file.write(text);
	file.close();

speaker=win32.Dispatch("SAPI.SpVoice");

def speak(fileName):
	file=open(fileName,'r');
	for text in file:
		speaker.Speak(text);
	file.close();

recognizer=SpeechRecognition.Recognizer();
def testSpeechRecognitionInForeground(timeout):
	with SpeechRecognition.Microphone() as microphone:
		print("A moment of silence");
		recognizer.adjust_for_ambient_noise(microphone, duration=1);
		speaker.Speak("Say something!");
		try:
			audio=recognizer.listen(microphone, timeout);
			print("Trying to recognize audio");
			text=recognizer.recognize_google(audio);
			speaker.Speak("You just said: "+text+" right? "); 
			if(text.find("help")!=-1): speak("help.txt");
			elif(text.find("stop")!=-1): return 0;
			else: write("audio.txt", text);
		except SpeechRecognition.UnknownValueError:
			print("Google Speech Recognition could not understand audio");
		except SpeechRecognition.WaitTimeoutError:
			print("Google Speech Recognition could not hear anything");
		except SpeechRecognition.RequestError as error:
			print("Could not request results from Google Speech Recognition service; {0}".format(error));
	return 1;

iteration=0;
isRunning=1;
maxIteration=100;
while(isRunning==1): 
	iteration=iteration+1; 
	if(iteration>=maxIteration): isRunning=0;
	else: isRunning=testSpeechRecognitionInForeground(2);
