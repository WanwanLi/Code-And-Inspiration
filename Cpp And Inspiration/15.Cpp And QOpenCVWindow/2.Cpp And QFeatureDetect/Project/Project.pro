QT += widgets
INCLUDEPATH += C:\opencv\build\include
HEADERS += QMainWindow.h QOpenCVWindow.h
SOURCES += QMainWindow.cpp QOpenCVWindow.cpp main.cpp
LIBS += -LC:\opencv\build\x64\vc14\lib -lopencv_world320 -lopencv_world320d