#include "String.h"
#include "MySQL.h"
int main()
{
	getConnection("DesktopDatabase","root","11235813");
	String StructuredQueryLanguage="Update InformationTable Set Name=\'LU\' Where ID=52";
	executeUpdate(StructuredQueryLanguage);
	StructuredQueryLanguage="Select * From InformationTable Where Name=\'LU\'";
	ResultSet ResultSet1=executeQuery(StructuredQueryLanguage);
	while(next(ResultSet1))
	{
		String id=getString(ResultSet1,"ID");
		String name=getString(ResultSet1,"Name");
		printf("ID=%s\tName=%s\n",id,name);
	}
	closeConnection();
	return 0;
}
