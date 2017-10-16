#include <WinSock2.h>
#include <mysql.h>
#define Connection MYSQL*
#define ResultSet MYSQL_RES*
MYSQL_ROW mySQLRow;
Connection connection;
void getConnection(String database,String user,String password)
{
	connection=mysql_init(NULL);
	if(!connection)printf("Init the connection to MySQL error...\n");
	connection=mysql_real_connect(connection,"localhost",user,password,database,0,NULL,0);
	if(!connection)printf("Fail to connection to MySQL ...\n");
}

void executeUpdate(String StructuredQueryLanguage)
{
	mysql_query(connection,StructuredQueryLanguage);
}

ResultSet executeQuery(String StructuredQueryLanguage)
{
	mysql_query(connection,StructuredQueryLanguage);
	ResultSet resultSet=mysql_store_result(connection);
	return resultSet;
}
int next(ResultSet resultSet)
{
	mySQLRow=mysql_fetch_row(resultSet);
	if(mySQLRow==NULL)return 0;
	return 1;
}

String getString(ResultSet resultSet,String fieldName)
{
	int field=-1;
	MYSQL_FIELD* mySQLField=mysql_fetch_field_direct(resultSet,++field);
	while(mySQLField!=NULL)
	{
		if(equals(mySQLField->name,fieldName))break;
		mySQLField=mysql_fetch_field_direct(resultSet,++field);
	}
	if(mySQLRow[field]==NULL)return "null";
	else return (String)mySQLRow[field];
}
void closeConnection()
{
	mysql_close(connection);
}
