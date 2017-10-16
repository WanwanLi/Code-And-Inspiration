#include <QApplication>
#include <QPushButton>
#include <QLabel >
#include <QTranslator>
#include <QFontDatabase>
#include <QTextCodec>
#include <QFile>
#include <QTextStream>
int main(int argc, char *argv[])
{
    QApplication app(argc, argv);

  QFile file("s.txt");
  if (!file.open(QIODevice::ReadOnly | QIODevice::Text))return -1;
	QStringList stringList;
    QTextStream in(&file);
	in.setCodec("UTF-8");
    while(!in.atEnd())
    {
        QString line = in.readLine();
        stringList<<line;
    }

    QTranslator translator;
    translator.load("hellotr_la");
    app.installTranslator(&translator);
// QTextCodec::setCodecForCStrings(QTextCodec::codecForName("UTF-8"));
//	QTextCodec::setCodecForLocale(QTextCodec::codecForName("UTF-8"));
	QString utf8_string = QStringLiteral("BEGIN OF TEXT | 是不存在中文支持 | END OF TEXT");
    QPushButton hello(stringList[0]);
    hello.resize(500, 300);
	int id=QFontDatabase::addApplicationFont("font.ttf");
QString family = QFontDatabase::applicationFontFamilies(id).at(0);
    hello.setFont(QFont(family, 50));
   // hello.setFont(QFont("Microsoft YaHei"));
    hello.show();
    return app.exec();
}