package mdt.aas.model;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public final class Optional<T> extends AbstractList<T> implements List<T> {
	@Nullable private final T m_data;
	
	public static <T> Optional<T> empty() {
		return new Optional<>(null);
	}
	
	public static <T> Optional<T> of(T data) {
		Objects.requireNonNull(data);
		return new Optional<>(data);
	}
	public static <T> Optional<T> ofNullable(T data) {
		return (data != null) ? new Optional<>(data) : empty();
	}
	
	public static <T> Optional<T> from(List<T> list) {
		Objects.requireNonNull(list);
		Preconditions.checkArgument(list.size() <= 1);
		return (list.size() > 0) ? new Optional<>(list.get(0)) : empty();
	}
	
	private Optional(T data) {
		m_data = data;
	}

	@Override
	public int size() {
		return isDefined() ? 1 : 0;
	}
	
	public @Nullable T get() {
		return m_data;
	}

	@Override
	public T get(int index) {
		if ( isEmpty() || index > 0 ) {
			throw new IndexOutOfBoundsException(String.format("index=%d, but size=%d", index, size()));
		}
		else {
			return m_data;
		}
	}
	
	public boolean isDefined() {
		return m_data != null;
	}
	
	public boolean isEmpty() {
		return m_data == null;
	}
	
	public List<T> asList() {
		return isDefined() ? Arrays.asList(m_data) : Collections.emptyList();
	}
	
	@Override
	public String toString() {
		return isDefined() ? m_data.toString() : "empty";
	}
}
