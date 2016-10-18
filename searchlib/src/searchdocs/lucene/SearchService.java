/*
 * 
 */
package searchdocs.lucene;

/**
 *
 * @author ayrat
 */
public class SearchService {
   
    private final IndexManager iman;
    private final SearchManager sman;
    // Конструктор принимающий папку индекса

    public SearchService(String index) {
        this.index=index;
        
    }
    
    
}
