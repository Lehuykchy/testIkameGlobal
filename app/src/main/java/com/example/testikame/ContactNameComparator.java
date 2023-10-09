package com.example.testikame;

import com.example.testikame.model.ContactInfo;

import java.util.Comparator;

public class ContactNameComparator implements Comparator<ContactInfo> {
    @Override
    public int compare(ContactInfo contactInfo1, ContactInfo contactInfo2) {
        if (contactInfo1.getFullnamePerson().isEmpty()) {
            return 1;
        } else if (contactInfo2.getFullnamePerson().isEmpty()) {
            return -1;
        } else {
            return contactInfo1.getFullnamePerson().compareToIgnoreCase(contactInfo2.getFullnamePerson());
        }

    }
}
