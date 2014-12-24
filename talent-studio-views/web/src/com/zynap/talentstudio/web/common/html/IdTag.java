package com.zynap.talentstudio.web.common.html;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;


/**
 * Tag library that generates valid values for html id attributes based on a
 * given text string. It does this by stripping all non-alphabetic or numeric
 * characters from the string and ensuring that the resulting id starts with a
 * letter. There is no guarantee that the resulting id value will be unique on
 * the html page.
 *
 * @author Alan Mynard
 */

public class IdTag
	extends BodyTagSupport
{
	/**
	 * The value to make into an id.
	 */

	private String value;

	/**
	 * Setter for value attribute.
	 *
	 * @param value The new value
	 */

	public void setValue(String value)
	{
		this.value = value;
	}

	public int doAfterBody()
		throws JspException
    {
		String x = bodyContent.getString();

		if ((x != null) && (x.length() != 0))
		{
			value = x;
		}

		return SKIP_BODY;
	}

	/**
	 * Writes out supplied value in id form.
	 *
	 * @return EVAL_PAGE
	 */

	public int doEndTag()
		throws JspException
	{
		JspWriter out = pageContext.getOut();

		try
		{
			out.print(stringToHtmlId(value));
		}
		catch (Throwable e)
		{
			throw new JspException(e);
		}

		return EVAL_PAGE;	
	}

	/**
	 * Convert any string into a valid HTML id string.
	 */

	static public String stringToHtmlId(String value)
	{
		String id = null;
        String pref = null;

		if ( value != null)
		{
			char x;
			int n = value.length();
			char str[] = new char[n];
			int idx = 0;

			for (int i = 0; i < n; i++)
			{
				x = value.charAt(i);

				if (Character.isLetterOrDigit(x) || x == '_' || x == '.' || x == ':'|| x == '-')
				{
					// id must start with a letter

					if ((idx == 0) && ! Character.isLetter(x))
					{
						pref ="i";
					}

					str[idx++] = x;
				}
			}

			if (idx != 0)
			{
				id = new String(str, 0, idx);
			}
		}

		if (id == null)
		{
			id = "zynapid";
		}
        if (pref!=null) id = pref + id;             
		return id;
	}
}
