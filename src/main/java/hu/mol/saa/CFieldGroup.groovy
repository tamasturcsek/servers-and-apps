package hu.mol.saa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Validator
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.validator.BeanValidator
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Field;
public class CFieldGroup<T> extends BeanFieldGroup<T> {
	private static final Logger logger = LoggerFactory.getLogger(CFieldGroup.class);
	/* azért kell a copy, mert az ősben private a beanType */
	private Class<T> beanTypeCopy;

	public CFieldGroup(Class<T> beanType) {
		super(beanType);
		beanTypeCopy = beanType;
	}

	public CFieldGroup(T bean) {
		this((Class<T>) bean.getClass());
		setItemDataSource(bean);
	}

	@Override
	public void bind(Field field, Object propertyId) {
		super.bind(field, propertyId);
		configureNullRepresentation(field);
		configureFieldByDeclaredAnnotations(field, propertyId);
	}
	private void configureNullRepresentation(Field field) {
		if (field instanceof AbstractTextField) {
			((AbstractTextField)field).setNullRepresentation("");
		}
	}

	/**
	 * A propertyn beállított annotációk alapján konfigurálja a fieldet. pl: @NotNull -> required field
	 */
	private void configureFieldByDeclaredAnnotations(final Field field, Object propertyId) {
		try {
			for (Validator validator : field.getValidators()) {
				if (validator instanceof BeanValidator) {
					((BeanValidator)validator).setLocale(SAAUI.getCurrent().getLocale());
				}
			}
		} catch (NoSuchFieldException nsfex) {
			/* valószínűleg nested property-n vagyunk */
			logger.trace("Annotáció konfiguráció hiba, valószínűleg nested property-n vagyunk", nsfex);
		} catch (Exception e) {
			/* elnyeljük az exceptiont */
			logger.error("Hiba annotáció konfigurálásakor", e);
		}

        try {
            java.lang.reflect.Field memberField = beanTypeCopy.getDeclaredField((String) propertyId);
            if (memberField.isAnnotationPresent(javax.validation.constraints.NotNull.class)) {
                field.setRequired(true);
                field.setRequiredError("A mező kitöltése kötelező");
            }

        } catch (NoSuchFieldException nsfex) {
            /* valószínűleg nested property-n vagyunk */
            logger.trace("Annotáció konfiguráció hiba, valószínűleg nested property-n vagyunk", nsfex);
        } catch (Exception e) {
            /* elnyeljük az exceptiont */
            logger.error("Hiba annotáció konfigurálásakor", e);
        }
	}
	/**
	 * Eredeti funkció megtartva, de úgy, hogy minden nem valid fieldnek adjon egy stílust. Ehhez az kell, hogy minden fieldet végigvalidáljon, ne csak az első invalidig menjen
	 */
	@Override
	public boolean isValid() {
		boolean valid = true;
		for (Field<?> field : getFields()) {
			try {
				field.validate();
				field.removeStyleName("invalid-value");
			} catch (InvalidValueException e) {
				valid = false;
				field.addStyleName("invalid-value");
			}
		}
		return valid;
	}
	public T getBean() {
		if (getItemDataSource() == null) {
			return null;
		}
		return getItemDataSource().getBean();
	}
}
