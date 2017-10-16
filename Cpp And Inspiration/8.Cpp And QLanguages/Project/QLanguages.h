#include <QHash>

class QLanguages
{
	public:
	void loadEnglish();
	bool load(QString language);
	QString translate(QString string);
	QString getEnglish(QString string);

	private:
	QHash<QString, QString> hash, table;
};