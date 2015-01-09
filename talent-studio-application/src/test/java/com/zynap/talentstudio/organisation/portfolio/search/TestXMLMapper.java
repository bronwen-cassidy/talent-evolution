package com.zynap.talentstudio.organisation.portfolio.search;

import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.organisation.portfolio.PortfolioItem;

import java.util.Iterator;
import java.util.List;

/**
 * User: amark
 * Date: 11-Jul-2006
 * Time: 16:35:32
 */
public class TestXMLMapper extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        mapper = (IMapper) getBean("xmlMapper");
    }

    public void testMap() throws Exception {

        final ISearchResult result = mapper.map(new ResultMapper(getExampleResultsXml()));
        final List hits = result.getHits();
        assertEquals(2, hits.size());
    }

    public void testMap_Details() throws Exception {

        final ISearchResult result = mapper.map(new ResultMapper(getExampleResultsXml()));
        final List hits = result.getHits();

        SearchResult previous = null;
        for (Iterator iterator = hits.iterator(); iterator.hasNext();) {
            SearchResult searchResult = (SearchResult) iterator.next();
            if (previous == null) {
                previous = searchResult;
            } else {
                // assert previous.getArtefactTitle != searchResult.getArtefactTitle
                assertNotSame(previous.getArtefactTitle(), searchResult.getArtefactTitle());
                previous = searchResult;
            }
        }
    }

    public void testMapScopePresent() throws Exception {

        final ISearchResult result = mapper.map(new ResultMapper(getExampleResultsXml()));
        List hits = result.getHits();
        SearchResult searchResult = (SearchResult) hits.iterator().next();
        assertEquals(PortfolioItem.PUBLIC_SCOPE, searchResult.getScope());
    }

    public void testGetResults() throws Exception {
        String xml = getValidXMLString();

        ResultMapper resultMapper = new ResultMapper(xml);

        XMLMapper mapper = new XMLMapper();
        ISearchResult searchResult = mapper.map(resultMapper);
        int hits = searchResult.getNumHits();
        assertEquals(4, hits);
    }

    private String getValidXMLString() {
        return "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?> \n" +
                "<autnresponse>\n" +
                "  <action>QUERY</action> \n" +
                "  <response>SUCCESS</response> \n" +
                " <responsedata xmlns:autn=\"http://schemas.autonomy.com/aci/\">\n" +
                "  <autn:numhits>4</autn:numhits> \n" +
                " <autn:hit>\n" +
                "  <autn:reference>C:\\bronwen\\ApacheGroup\\Apache2\\htdocs\\tango\\static\\test6.html</autn:reference> \n" +
                "  <autn:id>4</autn:id> \n" +
                "  <autn:weight>96.00</autn:weight> \n" +
                "  <autn:database>common</autn:database> \n" +
                "  <autn:title>1111111</autn:title> \n" +
                "  </autn:hit>\n" +
                " <autn:hit>\n" +
                "  <autn:reference>C:\\bronwen\\ApacheGroup\\Apache2\\htdocs\\tango\\static\\test5.html</autn:reference> \n" +
                "  <autn:id>3</autn:id> \n" +
                "  <autn:weight>96.00</autn:weight> \n" +
                "  <autn:database>common</autn:database> \n" +
                "  <autn:title>title</autn:title> \n" +
                "  </autn:hit>\n" +
                " <autn:hit>\n" +
                "  <autn:reference>C:\\bronwen\\ApacheGroup\\Apache2\\htdocs\\tango\\static\\test4.html</autn:reference> \n" +
                "  <autn:id>2</autn:id> \n" +
                "  <autn:weight>96.00</autn:weight> \n" +
                "  <autn:database>common</autn:database> \n" +
                "  </autn:hit>\n" +
                " <autn:hit>\n" +
                "  <autn:reference>C:\\bronwen\\ApacheGroup\\Apache2\\htdocs\\tango\\static\\test3.html</autn:reference> \n" +
                "  <autn:id>1</autn:id> \n" +
                "  <autn:weight>96.00</autn:weight> \n" +
                "  <autn:database>common</autn:database> \n" +
                "  </autn:hit>\n" +
                "  </responsedata>\n" +
                "  </autnresponse>";
    }

    private String getExampleResultsXml() {
        return "<autnresponse xmlns:autn=\"http://schemas.autonomy.com/aci/\">\n" +
                "<action>QUERY</action>\n" +
                "<response>SUCCESS</response>\n" +
                "\t<responsedata>\n" +
                "<autn:numhits>2</autn:numhits>\n" +
                "\t<autn:hit>\n" +
                "<autn:reference>9</autn:reference>\n" +
                "<autn:id>78602</autn:id>\n" +
                "<autn:section>0</autn:section>\n" +
                "<autn:weight>96.00</autn:weight>\n" +
                "<autn:database>positiondata</autn:database>\n" +
                "<autn:title>Default Position</autn:title>\n" +
                "\t<autn:content>\n" +
                "\t<DOCUMENT>\n" +
                "<DREREFERENCE>9  </DREREFERENCE>\n" +
                "<DRETITLE>Default Position</DRETITLE>\n" +
                "<DREDATE>1116839458</DREDATE>\n" +
                "<DREDBNAME>positiondata</DREDBNAME>\n" +
                "<COMPANY>ATCOM</COMPANY>\n" +
                "\t<DREFULLFILENAME>\n" +
                "C:\\Autonomy\\OracleFetch\\PositionFileContentItem\\57\\pos_fileitem_autonomy_view9.doc\n" +
                "</DREFULLFILENAME>\n" +
                "<ITEM_ID>9</ITEM_ID>\n" +
                "<ARTEFACT_ID>1</ARTEFACT_ID>\n" +
                "<CREATED_BY>1</CREATED_BY>\n" +
                "<CONTENT_TITLE>default.position desc</CONTENT_TITLE>\n" +
                "<SCOPE>PUBLIC</SCOPE>\n" +
                "<TYPE>FILEITEM</TYPE>\n" +
                "<CONTENT_TYPE_ID>DESC</CONTENT_TYPE_ID>\n" +
                "<HTTP_URL/>\n" +
                "\t<SUMMARY>\n" +
                "Account Manager Job Purpose To ensure client projects are delivered on time, to brief and budget and that income is commensurate with effort exerted. Responsibilities The management of project profit including: Development of project costs Agreeing costs with clients Preparation of turnover and income projections Reviewing time spent on projects Selecting suppliers and sub-contractors Reviewing and approving subcontractors invoices Preparation of monthly invoices for clients The management of the project including: Developing an implementation timeplan together with milestones Agreeing milestones and timeplan with client Establishing the project team Co-ordinating a start-up meeting and project brief for the project team Ensuring adequate briefing of all project team members on requirements and expectations Reviewing frequently the progress of the project against the timeplan and modifying the timeplan accordingly Developing an Operational Specification for the project in co-operation with the project team technical, programming, creative and design resource Agreeing the Operational Specification with the client Safeguarding against ‘feature creep’, but agreeing sensible and appropriate changes to the Operational Specification with the client Regular liaison and contact with client Managing client expectations and understanding to ensure that the finished project exceeds their expectation Managing the activity of the project team to ensure quality, time and cost standards are meet Analysis of the client requirement Regular liaison with the client including\n" +
                "</SUMMARY>\n" +
                "\t<DRECONTENT>\n" +
                "Account Manager\n" +
                "Job Purpose\n" +
                "To ensure client projects are delivered on time, to brief and budget and that income is commensurate with effort exerted.\n" +
                "Responsibilities\n" +
                "The management of project profit including:\n" +
                "Development of project costs\n" +
                "Agreeing costs with clients\n" +
                "Preparation of turnover and income projections\n" +
                "</DRECONTENT>\n" +
                "</DOCUMENT>\n" +
                "</autn:content>\n" +
                "</autn:hit>\n" +
                "\t<autn:hit>\n" +
                "<autn:reference>16</autn:reference>\n" +
                "<autn:id>78601</autn:id>\n" +
                "<autn:section>0</autn:section>\n" +
                "<autn:weight>96.00</autn:weight>\n" +
                "<autn:database>positiondata</autn:database>\n" +
                "<autn:title>Marketing salesman 1</autn:title>\n" +
                "\t<autn:content>\n" +
                "\t<DOCUMENT>\n" +
                "<DREREFERENCE>16  </DREREFERENCE>\n" +
                "<DRETITLE>Marketing salesman 1</DRETITLE>\n" +
                "<DREDATE>1116839458</DREDATE>\n" +
                "<DREDBNAME>positiondata</DREDBNAME>\n" +
                "<COMPANY>Carlton Online</COMPANY>\n" +
                "\t<DREFULLFILENAME>\n" +
                "C:\\Autonomy\\OracleFetch\\PositionFileContentItem\\550\\pos_fileitem_autonomy_view16.doc\n" +
                "</DREFULLFILENAME>\n" +
                "<ITEM_ID>16</ITEM_ID>\n" +
                "<ARTEFACT_ID>5</ARTEFACT_ID>\n" +
                "<CREATED_BY>1</CREATED_BY>\n" +
                "<CONTENT_TITLE>marketing.salesman competency</CONTENT_TITLE>\n" +
                "<SCOPE>PUBLIC</SCOPE>\n" +
                "<TYPE>FILEITEM</TYPE>\n" +
                "<CONTENT_TYPE_ID>CF</CONTENT_TYPE_ID>\n" +
                "<HTTP_URL/>\n" +
                "\t<SUMMARY>\n" +
                "Managing Editor - TV x 1 (salary to £35k) Reporting to: Executive Producer, Carlton.com  Job Description: This role would be to manage a channel (potentially 2 channels) with a mainstream entertainment focus\n" +
                "</SUMMARY>\n" +
                "\t<DRECONTENT>\n" +
                "Managing Editor - TV x 1 (salary to £35k)\n" +
                "Reporting to: Executive Producer, Carlton.com \n" +
                "Job Description:\n" +
                "This role would be to manage a channel (potentially 2 channels) with a mainstream entertainment focus. The Managing Editor would be responsible for developing the strategy and content development plan with the Executive Producer. The role would involve the management of an editorial team. Content would to include daily listings and reviews, programme support sites, in-depth features and interactive applications\n" +
                "To manage the content development of a channel \n" +
                "To manage and develop an editorial team\n" +
                "To produce the Channel product development plan and overall strategy\n" +
                "To be responsible for overall quality of the channel\n" +
                "</DRECONTENT>\n" +
                "</DOCUMENT>\n" +
                "</autn:content>\n" +
                "</autn:hit>" +
                "\t</responsedata>\n" +
                "</autnresponse>";
    }

    private IMapper mapper;
}
