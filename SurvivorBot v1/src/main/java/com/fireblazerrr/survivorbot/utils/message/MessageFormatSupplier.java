package com.fireblazerrr.survivorbot.utils.message;

public interface MessageFormatSupplier {
    String getStandardFormat();

    String getConversationFormat();

    String getAnnounceFormat();

    String getEmoteFormat();
}
