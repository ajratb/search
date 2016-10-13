/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.npopm.dep715.searchdocs.lucene;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

/**
 * Модуль поиска, работающий с библиотеками Lucene
 *
 * Создан на основе org.apache.lucene.demo.SearchFiles
 *
 * ГЛОБАЛЬНЫЕ ПЕРЕМЕННЫЕ (доступ через методы-геттеры): resultCount - количество
 * найденных файлов operationTime - время осуществления поиска results - список
 * найденных документов
 *
 * @author ayrat
 */
public class SearchFiles {

//    private static int resultCount;
    private static long operationTime;
    private static List<Document> results;

//    public List getResults() {
//        return results;
//    }
//
//    public int getResultCount() {
//        return resultCount;
//    }

    public long getOperationTime() {
        return operationTime;
    }

    private SearchFiles() {

    }

    /**
     *
     *
     *
     * @param indexPath
     * @param queryString
     * @return
     * @throws IOException
     * @throws IllegalArgumentException
     * @throws ParseException
     */
    public static List<Document> getSearchDocuments(String indexPath, String queryString)
            throws Exception {//IOException, IllegalArgumentException, ParseException {
        System.out.println("INSIDE SEARCHFILES CLASS:/n "+indexPath + " " + queryString);
//        indexPath="d:\\Index\\";
        
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new StandardAnalyzer();

        QueryParser parser = new QueryParser("contents", analyzer);

        Query query = parser.parse(queryString);

        System.out.println("Searching for: " + query.toString("contents"));
        
        try {
            doSearch(searcher, query, true);
        } catch (IllegalArgumentException noResultEx) {
//            resultCount = 0;
// пометить в логах что ниче не найдено
        }
        reader.close();
        return results;
    }

    private static void doSearch(IndexSearcher searcher, Query query,
            boolean raw) throws IOException, IllegalArgumentException {

        Date start = new Date();
        results=new ArrayList<>();
        TopDocs topDocs = searcher.search(query, 10);

        int numTotalHits = topDocs.totalHits;

        System.out.println("Total: " + numTotalHits);

        ScoreDoc[] hits = searcher.search(query, numTotalHits).scoreDocs;        

        for (int i = 0; i < numTotalHits; i++) {
            if (raw) {                              // output raw format
                System.out.println("doc=" + hits[i].doc + " score=" + hits[i].score);
//                continue;
            }

            Document doc = searcher.doc(hits[i].doc);
            results.add(doc);
            String path = doc.get("path");
            
            if (path != null) {
                System.out.println((i + 1) + ". " + path);
                String title = doc.get("title");
                if (title != null) {
                    System.out.println("   Title: " + doc.get("title"));
                }
            } else {
                System.out.println((i + 1) + ". " + "No path for this document");
            }
        }
        
        Date end = new Date();
        operationTime=end.getTime()-start.getTime();
    }
}
