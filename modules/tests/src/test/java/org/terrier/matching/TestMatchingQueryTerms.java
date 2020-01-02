/*
 * Terrier - Terabyte Retriever 
 * Webpage: http://terrier.org 
 * Contact: terrier{a.}dcs.gla.ac.uk
 * University of Glasgow - School of Computing Science
 * http://www.gla.ac.uk/
 * 
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and limitations
 * under the License.
 *
 * The Original Code is TestMatchingQueryTerms.java.
 *
 * The Original Code is Copyright (C) 2004-2019 the University of Glasgow.
 * All Rights Reserved.
 *
 * Contributor(s):
 *   Craig Macdonald <craigm{a.}dcs.gla.ac.uk> (original author)
 *   
 */
package org.terrier.matching;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.terrier.matching.matchops.SingleTermOp;
import org.terrier.querying.parser.Query;
import org.terrier.querying.parser.Query.QueryTermsParameter;
import org.terrier.querying.parser.QueryParser;
import org.terrier.querying.parser.Query.QTPBuilder;
import org.terrier.structures.BasicLexiconEntry;
import org.terrier.structures.EntryStatistics;

/** Test MatchingQueryTerms behaves as expected, in particular that order is retained.
 * Tests are separated into two groups: using the query parser ("Parsed"), and directly accessing
 * the MatchingQueryTerms object ("Direct)".
 * @since 3.0 
 */
public class TestMatchingQueryTerms {

	
	
	@Test public void checkDirectSingleTermAdd()
	{
		final String term = "term1";
		MatchingQueryTerms mqt = new MatchingQueryTerms();
		assertEquals(0, mqt.size());
		assertNotNull(mqt.getMatchingTerms());
		assertEquals(0, mqt.getMatchingTerms().length);	
		mqt.addTermPropertyWeight(term, 1.0d);
		assertEquals(1, mqt.size());	
		assertEquals(1, mqt.getMatchingTerms().length);	
		assertEquals(term, mqt.getMatchingTerms()[0].toString());
		assertEquals(1.0d, mqt.getTermWeight(term), 0.0d);
		assertNull(mqt.getStatistics(term));
		EntryStatistics e = new BasicLexiconEntry(2, 1, 100);
		mqt.setTermProperty(term, e);
		assertEquals(2, mqt.getStatistics(term).getTermId());
		assertEquals(1, mqt.getStatistics(term).getDocumentFrequency());
		assertEquals(100, mqt.getStatistics(term).getFrequency());
	}
	
	@Test public void checkDirectSingleTermSet()
	{
		final String term = "term1";
		MatchingQueryTerms mqt = new MatchingQueryTerms();
		assertEquals(0, mqt.size());
		assertNotNull(mqt.getMatchingTerms());
		assertEquals(0, mqt.getMatchingTerms().length);	
		//default weight of a term is 1
		mqt.add(QTPBuilder.of(new SingleTermOp(term)).build());
		assertEquals(1.0d, mqt.getTermWeight(term), 0.0d);
		mqt.setTermProperty(term, 1);
		//set overwrite, should still be 1
		assertEquals(1.0d, mqt.getTermWeight(term), 0.0d);
		assertEquals(1, mqt.size());	
		assertEquals(1, mqt.getMatchingTerms().length);	
		assertEquals(term, mqt.getMatchingTerms()[0].toString());
		assertEquals(1.0d, mqt.getTermWeight(term), 0.0d);
		assertNull(mqt.getStatistics(term));
		EntryStatistics e = new BasicLexiconEntry(2, 1, 100);
		mqt.setTermProperty(term, e);
		assertEquals(2, mqt.getStatistics(term).getTermId());
		assertEquals(1, mqt.getStatistics(term).getDocumentFrequency());
		assertEquals(100, mqt.getStatistics(term).getFrequency());
	}
	
	@Test public void checkDirectTwoTerms()
	{
		final String term1 = "zebra";
		final String term2 = "crossing";
		MatchingQueryTerms mqt = new MatchingQueryTerms();
		mqt.addTermPropertyWeight(term1, 1.0d);
		mqt.addTermPropertyWeight(term2, 1.0d);
		assertEquals(2, mqt.size());	
		assertEquals(2, mqt.getMatchingTerms().length);	
		assertEquals(term1, mqt.getMatchingTerms()[0].toString());
		assertEquals(term2, mqt.getMatchingTerms()[1].toString());
		
		assertEquals(1.0d, mqt.getTermWeight(term1), 0.0d);
		assertEquals(1.0d, mqt.getTermWeight(term2), 0.0d);
		
		assertNull(mqt.getStatistics(term1));
		assertNull(mqt.getStatistics(term2));
				
		EntryStatistics e1 = new BasicLexiconEntry(2, 1, 100);
		EntryStatistics e2 = new BasicLexiconEntry(40, 100, 102);
		mqt.setTermProperty(term1, e1);
		mqt.setTermProperty(term2, e2);
		
		assertEquals(2, mqt.getStatistics(term1).getTermId());
		assertEquals(40, mqt.getStatistics(term2).getTermId());
		
		assertEquals(1, mqt.getStatistics(term1).getDocumentFrequency());
		assertEquals(100, mqt.getStatistics(term2).getDocumentFrequency());
		
		assertEquals(100, mqt.getStatistics(term1).getFrequency());
		assertEquals(102, mqt.getStatistics(term2).getFrequency());
	}
	
	@Test public void checkParsedSingleTerm() throws Exception
	{
		final String term = "term1";
		Query q = QueryParser.parseQuery(term);
		MatchingQueryTerms mqt = new MatchingQueryTerms();
		q.obtainQueryTerms(QueryTermsParameter.of(mqt, true));
		assertEquals(1, mqt.size());	
		assertEquals(1, mqt.getMatchingTerms().length);	
		assertEquals(term, mqt.getMatchingTerms()[0].toString());
		assertEquals(1.0d, mqt.getTermWeight(term), 0.0d);
		assertNull(mqt.getStatistics(term));
		EntryStatistics e = new BasicLexiconEntry(2, 1, 100);
		mqt.setTermProperty(term, e);
		assertEquals(2, mqt.getStatistics(term).getTermId());
		assertEquals(1, mqt.getStatistics(term).getDocumentFrequency());
		assertEquals(100, mqt.getStatistics(term).getFrequency());
	}
	
	@Test public void checkParsedSingleSynonym() throws Exception
	{
		String term = "{term1 term2}";	
		Query q = QueryParser.parseQuery(term);
		term = "#syn(term1 term2)"; //internal notation is different
		MatchingQueryTerms mqt = new MatchingQueryTerms();
		q.obtainQueryTerms(QueryTermsParameter.of(mqt, true));
		assertEquals(1, mqt.size());	
		assertEquals(1, mqt.getMatchingTerms().length);	
		assertEquals(term, mqt.getMatchingTerms()[0].toString());
		assertEquals(1.0d, mqt.getTermWeight(term), 0.0d);
		assertNull(mqt.getStatistics(term));
		EntryStatistics e = new BasicLexiconEntry(2, 1, 100);
		mqt.setTermProperty(term, e);
		assertEquals(2, mqt.getStatistics(term).getTermId());
		assertEquals(1, mqt.getStatistics(term).getDocumentFrequency());
		assertEquals(100, mqt.getStatistics(term).getFrequency());
	}
	
	@Test public void checkParsedRepeatedTerm() throws Exception
	{
		final String term1 = "zebra";
		Query q = QueryParser.parseQuery(term1 +" " + term1);
		MatchingQueryTerms mqt = new MatchingQueryTerms();
		q.obtainQueryTerms(QueryTermsParameter.of(mqt, true));
		assertEquals(1, mqt.size());	
		assertEquals(1, mqt.getMatchingTerms().length);	
		assertEquals(term1, mqt.getMatchingTerms()[0].toString());
		
		assertEquals(2d, mqt.getTermWeight(term1), 0.0d);
	}
	
	@Test public void checkParsedRepeatedTermWithDifferentReq() throws Exception
	{
		final String term1 = "zebra";
		Query q;
		MatchingQueryTerms mqt;
		q = QueryParser.parseQuery(term1 + " " + " +"+term1);
		mqt = new MatchingQueryTerms();
		q.obtainQueryTerms(QueryTermsParameter.of(mqt, true));
		//Semantics have changed with TREC-420
//		assertEquals(2, mqt.size());	
//		assertEquals(2, mqt.getMatchingTerms().length);	
		
		q = QueryParser.parseQuery(term1 + " " + " field:"+term1);
		mqt = new MatchingQueryTerms();
		q.obtainQueryTerms(QueryTermsParameter.of(mqt, true));
		assertEquals(2, mqt.size());	
		assertEquals(2, mqt.getMatchingTerms().length);	
		
	}
	
	
	@Test public void checkParsedRepeatedTermWithWeights() throws Exception
	{
		final String term1 = "zebra";
		Query q = QueryParser.parseQuery(term1 +"^0.1 " + term1 + "^0.2");
		MatchingQueryTerms mqt = new MatchingQueryTerms();
		q.obtainQueryTerms(QueryTermsParameter.of(mqt, true));
		assertEquals(1, mqt.size());	
		assertEquals(1, mqt.getMatchingTerms().length);	
		assertEquals(term1, mqt.getMatchingTerms()[0].toString());		
		assertEquals(0.3d, mqt.getTermWeight(term1), 0.001d);
	}
	
	@Test public void checkParsedTwoTerms() throws Exception
	{
		final String term1 = "zebra";
		final String term2 = "crossing";
		Query q = QueryParser.parseQuery(term1+"^0.5 " + term2 + "^1.0");
		MatchingQueryTerms mqt = new MatchingQueryTerms();
		q.obtainQueryTerms(QueryTermsParameter.of(mqt, true));
		assertEquals(2, mqt.size());	
		assertEquals(2, mqt.getMatchingTerms().length);	
		assertEquals(term1, mqt.getMatchingTerms()[0].toString());
		assertEquals(term2, mqt.getMatchingTerms()[1].toString());
		
		assertEquals(0.5d, mqt.getTermWeight(term1), 0.0d);
		assertEquals(1.0d, mqt.getTermWeight(term2), 0.0d);
		
		assertNull(mqt.getStatistics(term1));
		assertNull(mqt.getStatistics(term2));
				
		EntryStatistics e1 = new BasicLexiconEntry(2, 1, 100);
		EntryStatistics e2 = new BasicLexiconEntry(40, 100, 102);
		mqt.setTermProperty(term1, e1);
		mqt.setTermProperty(term2, e2);
		
		assertEquals(2, mqt.getStatistics(term1).getTermId());
		assertEquals(40, mqt.getStatistics(term2).getTermId());
		
		assertEquals(1, mqt.getStatistics(term1).getDocumentFrequency());
		assertEquals(100, mqt.getStatistics(term2).getDocumentFrequency());
		
		assertEquals(100, mqt.getStatistics(term1).getFrequency());
		assertEquals(102, mqt.getStatistics(term2).getFrequency());
	}
	
}
