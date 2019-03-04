import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BuildQuery {

	public BuildQuery() {
		
	}
	
	public static String buildQuery(String statementType, String table, HashMap<String,String> firstValues, HashMap<String,String> secondValues) {
		
		String sql = "";

		switch(statementType) {
			case "create":
				sql += "INSERT INTO " + table + " VALUES (" ;
				break;


			case "modify":
				sql += "UPDATE " + table + " SET ";
				break;


			case "view":
				sql += "SELECT * FROM " + table;
				break;


			case "delete":
				sql += "DELETE FROM "  + table;
				break;
		}

		switch(statementType) {
			case "create":
				String stCreate = "";

				for(int n = 0; n < firstValues.size(); n++) {
					stCreate += "?";

					if(n+1 < firstValues.size()) {
						stCreate += ",";
					} else {
						stCreate += ");";
					}
				}

				sql += stCreate;
				break;

			case "view":
				if(firstValues == null) {
					sql += ";";
					break;
				}
			case "delete":
				//For safety reasons, delete statements shouldn't be allowed to delete records like the above case can view all records.
				sql += " WHERE ";
			case "modify":
				String st = "";
				ArrayList<String> sts = new ArrayList<>();
				
					for(Map.Entry<String,String> firstValue: firstValues.entrySet()) {
					sts.add(firstValue.getKey() + "='" + firstValue.getValue() + "'");
				}

				for(int n = 0; n < sts.size(); n++) {
					st += sts.get(n);

					if(n+1 < sts.size()) {
						st += ", ";
					} else {
						st += ";";
					}
				}

				sql += st;
				
				if(!statementType.equals("modify")) { break; } //returns the string as is for view/delete statements.

				sql = sql.substring(0,sql.length()-1); //removes the ";" from the end of the statement.

				ArrayList<String> newSts = new ArrayList<>();
				st = "";
				st += " WHERE ";

				for(Map.Entry<String,String> secondValue: secondValues.entrySet()) {
					newSts.add(secondValue.getKey() + "='" + secondValue.getValue() + "'");
				}

				for(int n = 0; n < newSts.size(); n++) {
					st += newSts.get(n);

					if(n+1 < newSts.size()) {
						st += " AND ";
					} else {
						st += ";";
					}
				}
				
				sql += st;
		}
		
		return sql;
				
		
	}
}

