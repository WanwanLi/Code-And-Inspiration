#include <QFile>
#include <QTextStream>
#include "QLanguages.h"

bool QLanguages::load(QString language)
{
	QFile file("languages/"+language+".lang");
	if(!file.open(QIODevice::ReadOnly|QIODevice::Text))return false;
	QTextStream textStream(&file); textStream.setCodec("UTF-8");
	while(!textStream.atEnd())
	{
		QStringList stringList=textStream.readLine().split(" : ");
		if(stringList.length()==2)
		{
			this->hash[stringList[0]]=stringList[1];
			this->table[stringList[1]]=stringList[0];
		}
	}
	return true;
}
void QLanguages::loadEnglish()
{
	QHashIterator<QString, QString> iterator(hash);
	while(iterator.hasNext()) 
	{
		iterator.next();
		this->hash[iterator.key()]=iterator.key();
	}
}
QString QLanguages::translate(QString string)
{
	return hash.contains(string)?hash[string]:string;	
}
QString QLanguages::getEnglish(QString string)
{
	return table.contains(string)?table[string]:string;	
}
