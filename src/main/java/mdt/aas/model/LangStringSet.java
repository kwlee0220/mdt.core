package mdt.aas.model;

import java.util.AbstractList;
import java.util.List;
import java.util.ListIterator;

import com.google.common.collect.Lists;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class LangStringSet extends AbstractList<LangString> {
	private final List<LangString> m_lstrings = Lists.newArrayList();

	@Override
	public int size() {
		return m_lstrings.size();
	}

	@Override
	public LangString get(int index) {
		return m_lstrings.get(index);
	}
	
	@Override
    public void add(int index, LangString lstring) {
		m_lstrings.add(index, lstring);
    }
	
	@Override
    public boolean add(LangString lstring) {
		return m_lstrings.add(lstring);
	}

	@Override
    public ListIterator<LangString> listIterator() {
        return m_lstrings.listIterator();
    }
    
	@Override
    public ListIterator<LangString> listIterator(final int index) {
        return m_lstrings.listIterator(index);
	}
}
