package com.grepp.spring.app.model.reward.code;


import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ItemTypeConverter implements AttributeConverter<ItemType, String> {


    @Override
    public ItemType convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return ItemType.valueOf(dbData.toUpperCase()); // db → enum (대소문자 대응)
    }

    @Override
    public String convertToDatabaseColumn(ItemType attribute) {
        return attribute == null ? null : attribute.name(); // enum → DB
    }


}
