package com.crawljax.plugins.biofuzz.utils;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.owasp.webscarab.model.HttpUrl;
import org.owasp.webscarab.model.Message;
import org.owasp.webscarab.model.Request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.configuration.ProxyConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;

import com.crawljax.plugins.biofuzz.core.BioFuzzPlugin;
import com.crawljax.plugins.biofuzz.core.components.BioFuzzAutomation;

import com.crawljax.plugins.biofuzz.input.BioFuzzContent;
import com.crawljax.plugins.biofuzz.input.BioFuzzInputSpecIface;
import com.crawljax.plugins.biofuzz.input.BioFuzzParamTuple;
import com.crawljax.plugins.biofuzz.input.BioFuzzContent.ContentType;
import com.crawljax.plugins.biofuzz.proxy.BioFuzzProxy;

import flex.messaging.util.URLEncoder;


public class BioFuzzUtils {

	final static Logger logger = LoggerFactory.getLogger(BioFuzzUtils.class);
	final static BioFuzzAutomation bauto = new BioFuzzAutomation();
	final static Pattern paramPat = Pattern.compile("(\\w*)=(.*)",Pattern.CASE_INSENSITIVE);
	
	public static int getStrDist(String s1, String s2) {
		return BioFuzzLevenshtein.getLdist(s1, s2);
	}
	
	public static double getNormalziedStrDist(String s1, String s2) {
		return BioFuzzLevenshtein.getNormalizedLdist(s1, s2);
	}

	public static double max(double a, double b) {
		return a > b ? a : b;
	}
	
    public static String strArrayToStr(String [] sa) {
        String s = "";

        for(int i = 0; i < sa.length; i++) {
                s += sa[i] + ' ';
        }

        return s;

    }
    
    public static Document stringToDom(String xml) throws
    SAXException, ParserConfigurationException, IOException {
    	DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
    	fact.setNamespaceAware(false);
    	DocumentBuilder builder = fact.newDocumentBuilder();
    	
    	return builder.parse(new InputSource( new StringReader(xml)));	
    }
    
    public static String readFile(String file) throws IOException {
    	BufferedReader reader = new BufferedReader( new FileReader(file));
    	String line = null;
    	StringBuilder stringBuilder = new StringBuilder();
    	String ls = System.getProperty("line.separator");
    	
    	while((line = reader.readLine()) != null) {
    		stringBuilder.append( line );
    		stringBuilder.append(ls);
    	}
    	reader.close();
    	return stringBuilder.toString();
    }
    
    public static String domToString(Document doc) {
    	
    	TransformerFactory tfact = TransformerFactory.newInstance();
    	Transformer trans = null;
		try {
			trans = tfact.newTransformer();
		} catch (TransformerConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    	trans.setOutputProperty(OutputKeys.INDENT, "yes");
    	
    	StringWriter sw = new StringWriter();
    	StreamResult res = new StreamResult(sw);
    	DOMSource src = new DOMSource(doc);
    	try {
			trans.transform(src, res);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String sxml = sw.toString();
    	
    	return sxml;
    	
    }
   
    
    public static String getParentFormXPath (String path) {
		String [] t = path.split("//");
		
		String out = "";
		int i = 0;
		for(i = t.length-1; i >= 0; i--) {
			if (t[i].toUpperCase().matches("FORM.*") == true) {
				break;
			}
		}
		
		for (int k = 0; k <= i; k++ ) {
			out += t[k];
			
			if(k != i)
				out += "//";
		}
		
		return out;
		
    }
    
    public static String doGetXPathFromNode(Node node) {
    	assert(node != null);
    	String s = null;
    	logger.debug("getXPathFromNode : " + node.getNodeName());
    	//s = getXPathFromNode(node, "");
    	s = getShortXPathFromNode(node, "");
    	return s;
    }

    
    private static String getShortXPathFromNode(Node node, String path) {
    	if (node == null)
    		return "";
    			
    	String ename = "";
    	
    	if (node instanceof Element) {
    		int i = 0;
    		boolean append = false;
    		ename = ((Element)node).getNodeName();
    		
    		
    		if(!ename.equals("HTML") 
    				&& !ename.equals("TABLE") 
    				&& !ename.equals("TBODY") 
    				&& !ename.equals("TR") 
    				&& !ename.equals("TD")
    				&& !ename.equals("BR")
    				&& !ename.equals("UL")
    				&& !ename.equals("LI")
    				&& !ename.equals("P")
    				&& !ename.equals("DIV")) {
    		
	    		NamedNodeMap nmap = node.getAttributes();
	    		String alist = "[";
	    		for(i = 0; i < nmap.getLength(); i++) {

	    			Node attr = nmap.item(i);
	    			
	    			// filter out specific (dangerous) attributes
	    			if(attr.getNodeName().equals("onclick") || 
	    					attr.getNodeName().equals("value") ||
	    					attr.getNodeName().equals("onload"))
	    				continue;
	    			
	    			if(append) {
	    				alist += " and ";
	    			}
	    			
	    			alist += "@" + attr.getNodeName() + "='" + attr.getNodeValue() + "'";
	    			append = true;
	    		}
	    		alist += "]";
	    		if (i > 0)
	    			ename += alist;
    		} else {
    			
    			ename = "";
    		
    		}
    	}
    	
    	Node parent = node.getParentNode();
    	if(parent == null | ename.equals("HTML")) {
    		return path;
    	}
    	
    	String pfx = ename.length() > 0 ? "//" + ename : "";
    	
    	return getShortXPathFromNode(parent, pfx + path);
    }

    
    public static String getNormalizedDom(String html) {
    	
    	//String wPat = "[a-zA-Z0-9=\"\' ]*";
    	//String valPat = "VALUE=[\"\']" + wPat + "[\"|\']";
    	//String txtPat = "TYPE=[\"\']TEXT[\"|\']";
    	//String pwdPat = "TYPE=[\"\']TEXT[\"|\']";
    	
    	String s = Pattern.quote(html.toUpperCase());
    	//String s = html;
    	
    	s = s.replaceAll("\t", " ");
    	s = s.replaceAll("\n", " ");
    	s = s.replaceAll(" +", " ");
    	//s = s.replaceAll("<SCRIPT.*>.*</SCRIPT>", "");
    	s = s.replaceAll("> *", ">");
    	s = s.replaceAll(" *<", "<");
    	
    	/**s = s.replaceAll("(< *INPUT" + wPat + " " + txtPat + wPat + ") " + valPat +"(" + wPat + ">)","$1 VALUE='' $2");
    	s = s.replaceAll("(< *INPUT" + wPat + ") " + valPat + "( " + wPat + txtPat  + wPat + ">)", "$1 VALUE='' $2");
    	
    	s = s.replaceAll("(< *INPUT" + wPat + " " + pwdPat + wPat + ") " + valPat +"(" + wPat + ">)","$1 VALUE='' $2");
    	s = s.replaceAll("(< *INPUT" + wPat + ") " + valPat + "( " + wPat + pwdPat  + wPat + ">)", "$1 VALUE='' $2");**/
    	
    	s = s.replaceAll("(<\\w+)[^>]*(>)","$1$2");
    	
    	return s;
    }
    
    
    private static CrawljaxConfigurationBuilder getConfigFor(String url) {
    	CrawljaxConfigurationBuilder builder = CrawljaxConfiguration.builderFor(url);
    	
		return builder;
    }
    
    private static CrawljaxConfigurationBuilder getConfigFor(String url, BioFuzzInputSpecIface ispec) {
    	CrawljaxConfigurationBuilder builder = getConfigFor(url);
    	if(ispec != null && ispec.getInputSpec() != null)
    		builder.crawlRules().setInputSpec(ispec.getInputSpec());
    	return builder;
    }
    
    public static CrawljaxConfiguration getConfigFor(String url, BioFuzzInputSpecIface ispec, BioFuzzProxy proxy) {
		
    	CrawljaxConfigurationBuilder builder = getConfigFor(url, ispec);
			
		builder.setProxyConfig(ProxyConfiguration.manualProxyOn(proxy.getHost(), proxy.getPort()));
		
		return builder.build();
    	
    }
    
    public static CrawljaxConfiguration getConfigFor(String url, BioFuzzInputSpecIface ispec, BioFuzzPlugin plugin) {
		
    	BioFuzzProxy proxy = plugin.getProxy();
    	
    	CrawljaxConfigurationBuilder builder = getConfigFor(url, ispec);
    	logger.debug("set proxy config :" + proxy.getHost() + ":" + proxy.getPort());
    	builder.setProxyConfig(ProxyConfiguration.manualProxyOn(proxy.getHost(), proxy.getPort()));
    	
		builder.addPlugin(plugin);
		
		return builder.build();
    	
    }
    
    public static CrawljaxConfigurationBuilder getConfigBuilderFor(String url, BioFuzzInputSpecIface ispec, BioFuzzPlugin plugin) {
		
    	BioFuzzProxy proxy = plugin.getProxy();
    	
    	CrawljaxConfigurationBuilder builder = getConfigFor(url, ispec);
    	logger.debug("set proxy config :" + proxy.getHost() + ":" + proxy.getPort());
    	builder.setProxyConfig(ProxyConfiguration.manualProxyOn(proxy.getHost(), proxy.getPort()));
    	
		builder.addPlugin(plugin);
		
		return builder;
    	
    }
    
    public static CrawljaxConfiguration getConfigFor(String url, BioFuzzProxy proxy) {
		
    	CrawljaxConfigurationBuilder builder = getConfigFor(url);

		builder.setProxyConfig(ProxyConfiguration.manualProxyOn(proxy.getHost(), proxy.getPort()));
		
		return builder.build();
    	
    }
    
    public static CrawljaxConfiguration getConfigFor(String url, BioFuzzInputSpecIface ispec, String proxyURL, int proxyPort) {
		
    	CrawljaxConfigurationBuilder builder = getConfigFor(url,ispec);
    		
		builder.setProxyConfig(ProxyConfiguration.manualProxyOn(proxyURL, proxyPort));
		
		return builder.build();
    }
    
    
    public static CrawljaxConfiguration getConfigFor(String url, String proxyURL, int proxyPort) {
		
    	CrawljaxConfigurationBuilder builder = getConfigFor(url);
  
		builder.setProxyConfig(ProxyConfiguration.manualProxyOn(proxyURL, proxyPort));
		
		return builder.build();
    }
    
    
    public static HashMap<String,List<String>> getTableInfo(String s) {
    
		HashMap<String,List<String>> pat = new HashMap<String,List<String>>();
		
		String termSplit = s.replaceAll(" *([^ ,=]* *= *['\"][^'\"=]*['\"])[ *,?]", "\t$1\n");
		termSplit = termSplit.replaceAll("[^\t]*\t(.*)\n[^\n]*", "$1\n");
		
		String [] cols = termSplit.split("\n");
		
		for(String col : cols) {
			String key = col.replaceAll("([^=]*)=.*", "$1");
			logger.debug("col: " + col);
			if(pat.containsKey(key)) {
				pat.get(key).add(col);
			} else {
				List <String> v = new Vector<String>();
				v.add(col);
				pat.put(key,v);
			}
		}
		
		return pat;
    
    }
    
    
    public static BioFuzzContent createAndGetContent(Request req) {
    	assert(req != null);
    	
    	logger.debug("Request " + req.toString());
    	
    	ContentType type = ContentType.UNDEF;
    	
    	String method = req.getMethod();
    	
    	String params = null;
    	
    	if(method != null && method.equals("GET")) {
    		type = ContentType.GET;
    		
    		HttpUrl url = req.getURL();
    		
    		if(url != null) {
    			params = url.getParameters();    			
    		}
    	}
    	
    	if(method != null && method.equals("POST")) {
    		type = ContentType.POST;
        	byte[] content = req.getContent();
 
    		if(content != null && content.length >= 0) {
    			params = new String(content);
    			logger.debug("Content to string " + params);
    		}
    		
    	}
    	
		// remove leading ? from the url (only important for GET)
		if(params.startsWith("?")) {
			params = params.substring(1, params.length()-1);
		}
		
		String[] tuples = params.split("&");
		
		logger.debug(" tuple list " + tuples[0]);
		
		List<BioFuzzParamTuple> ptuples = new Vector<BioFuzzParamTuple>();
		
		for(int i = 0; i < tuples.length; i++) {
			assert(tuples[i] != null);

			Matcher match = paramPat.matcher(tuples[i]);

			BioFuzzParamTuple ptup = new BioFuzzParamTuple(match.replaceAll("$1"), match.replaceAll("$2"));
			ptuples.add(ptup);
		}
		
		
		return new BioFuzzContent(ptuples, type);
    }
    
    public static Request fuzzAndGetRequest (Request r, BioFuzzContent con) {
    	
    	// create a copy
    	Request req = new Request(r);
    	
    	logger.debug(con.toString());
    	
    	assert(req.getMethod().equals(con.getContentType().toString()));
    	
    	String method = req.getMethod();
    	
    	if(method != null && method.equals("POST") && con.getContentType() == ContentType.POST) {
    		req.setContent(con.toString().getBytes());
    		return req;
    	} else if (method != null && method.equals("GET") && con.getContentType() == ContentType.GET) {
    	
    		HttpUrl url = req.getURL();
    		
    		if(url != null) {
    			String surl = url.toString();
    			assert(surl != null);
    			
    			if(surl != null && surl.length() > 0) {
    				String [] surlSeg = surl.split("\\?");
    				String snewUrl = surlSeg[0] + con.toString();
    				HttpUrl newUrl;
					try {
						newUrl = new HttpUrl(snewUrl);
					} catch (MalformedURLException e) {
						logger.debug("Cannot change url " + e.getMessage());
						return null;
					}
					
    				req.setURL(newUrl);
    				return req;
    			}
    		}
    	}
    	return null;
    }
    
}
