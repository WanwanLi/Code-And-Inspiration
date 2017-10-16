public class JavaAndXMLTable
{
	public static void main(String[] argv) 
	{
		String tableName="XMLTable", itemName="Asset";
		String[] tableAttributes={"outcome", "type", "timestamp"};
		String[] itemAttributes={"name", "serialNumber", "elapsedTime"};
		XMLTable xmlTable=new XMLTable(tableName, tableAttributes, itemName, itemAttributes);
		xmlTable.printAttributes();
		xmlTable.printItems();
		xmlTable.writeJSONFile();
	}
}



