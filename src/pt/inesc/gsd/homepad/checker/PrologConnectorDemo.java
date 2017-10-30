package pt.inesc.gsd.homepad.checker;

import java.util.List;
import java.util.Map;
 
import org.cs3.prolog.connector.Connector;
import org.cs3.prolog.connector.common.QueryUtils;
import org.cs3.prolog.connector.process.PrologProcess;
import org.cs3.prolog.connector.process.PrologProcessException;
 
/*
 * Prolog connector demo
 * 
 * 
 */


public class PrologConnectorDemo {
 
    public static void main(String[] args) {
        // get prolog process ... will be created if it doesn't already exist
        try {
            PrologProcess process = Connector.newPrologProcess();
 
            System.out.println(process.getExecutablePath());
            process.setExecutablePath("/usr/local/Cellar/swi-prolog/7.2.3/libexec/bin/swipl");
            
            // fill the factbase
            fillFactbaseWithDemoData(process);
 
            // create query with the buildTerm method
            // this is the same as "father_of(Father, peter)"
            String query = QueryUtils.bT("father_of", "Father", "peter");
            // get the first result of the query (ignore other results if there
            // are any)
            Map<String, Object> result = process.queryOnce(query);
            if (result == null) {
                // if the result is null, the query failed (no results)
                System.out.println("peter has no father");
            } else {
                // if the query succeeds, the resulting map contains mappings
                // from variable name to the binding
                System.out.println(result.get("Father") + " is the father of peter");
            }
 
            // create another query: father_of(john, Child)
            query = QueryUtils.bT("father_of", "john", "Child");
            // get ALL results of the query as a list
            // every element in this list is one result
            // if the query fails, the list will be empty (but it won't be null)
            List<Map<String, Object>> results = process.queryAll(query);
            for (Map<String, Object> r : results) {
                // iterate over every result
                System.out.println(r.get("Child") + " is a child of john");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    private static void fillFactbaseWithDemoData(PrologProcess process) throws PrologProcessException {
        // this can be done by asserting facts directly
        process.queryOnce("assertz(father_of(paul, peter))");
        process.queryOnce("assertz(father_of(john, paul))");
        process.queryOnce("assertz(father_of(john, ringo))");
        process.queryOnce("assertz(father_of(john, george))");
 
        // or by consulting a file
        // String consultQuery = QueryUtils.bT("reconsult", "'c:/some_prolog_file.pl'");
        // process.queryOnce(consultQuery);
    }
}