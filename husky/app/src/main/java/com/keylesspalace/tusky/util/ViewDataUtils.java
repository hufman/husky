/* Copyright 2017 Andrew Dawson
 *
 * This file is a part of Tusky.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * Tusky is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Tusky; if not,
 * see <http://www.gnu.org/licenses>. */

package com.keylesspalace.tusky.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.keylesspalace.tusky.entity.Notification;
import com.keylesspalace.tusky.entity.Status;
import com.keylesspalace.tusky.entity.Chat;
import com.keylesspalace.tusky.entity.ChatMessage;
import com.keylesspalace.tusky.viewdata.NotificationViewData;
import com.keylesspalace.tusky.viewdata.StatusViewData;
import com.keylesspalace.tusky.viewdata.ChatViewData;
import com.keylesspalace.tusky.viewdata.ChatMessageViewData;

/**
 * Created by charlag on 12/07/2017.
 */

public final class ViewDataUtils {
    @Nullable
    public static StatusViewData.Concrete statusToViewData(@Nullable Status status,
                                                           boolean alwaysShowSensitiveMedia,
                                                           boolean alwaysOpenSpoiler) {
        if (status == null) return null;
        Status visibleStatus = status.getReblog() == null ? status : status.getReblog();
        return new StatusViewData.Builder().setId(status.getId())
                .setAttachments(visibleStatus.getAttachments())
                .setAvatar(visibleStatus.getAccount().getAvatar())
                .setContent(visibleStatus.getContent())
                .setCreatedAt(visibleStatus.getCreatedAt())
                .setReblogsCount(visibleStatus.getReblogsCount())
                .setFavouritesCount(visibleStatus.getFavouritesCount())
                .setInReplyToId(visibleStatus.getInReplyToId())
                .setInReplyToAccountAcct(visibleStatus.getInReplyToAccountAcct())
                .setFavourited(visibleStatus.getFavourited())
                .setBookmarked(visibleStatus.getBookmarked())
                .setReblogged(visibleStatus.getReblogged())
                .setIsExpanded(alwaysOpenSpoiler)
                .setIsShowingSensitiveContent(false)
                .setMentions(visibleStatus.getMentions())
                .setNickname(visibleStatus.getAccount().getUsername())
                .setRebloggedAvatar(status.getReblog() == null ? null : status.getAccount().getAvatar())
                .setSensitive(visibleStatus.getSensitive())
                .setIsShowingSensitiveContent(alwaysShowSensitiveMedia || !visibleStatus.getSensitive())
                .setSpoilerText(visibleStatus.getSpoilerText())
                .setRebloggedByUsername(status.getReblog() == null ? null : status.getAccount().getDisplayName())
                .setUserFullName(visibleStatus.getAccount().getName())
                .setVisibility(visibleStatus.getVisibility())
                .setSenderId(visibleStatus.getAccount().getId())
                .setRebloggingEnabled(visibleStatus.rebloggingAllowed())
                .setApplication(visibleStatus.getApplication())
                .setStatusEmojis(visibleStatus.getEmojis())
                .setAccountEmojis(visibleStatus.getAccount().getEmojis())
                .setRebloggedByEmojis(status.getReblog() == null ? null : status.getAccount().getEmojis())
                .setCollapsible(SmartLengthInputFilterKt.shouldTrimStatus(visibleStatus.getContent()))
                .setCollapsed(true)
                .setPoll(visibleStatus.getPoll())
                .setCard(visibleStatus.getCard())
                .setIsBot(visibleStatus.getAccount().getBot())
                .setMuted(visibleStatus.isMuted())
                .setUserMuted(visibleStatus.isUserMuted())
                .setThreadMuted(visibleStatus.isThreadMuted())
                .setConversationId(visibleStatus.getConversationId())
                .setEmojiReactions(visibleStatus.getEmojiReactions())
                .setParentVisible(visibleStatus.getParentVisible())
                .createStatusViewData();
    }

    public static NotificationViewData.Concrete notificationToViewData(Notification notification,
                                                                       boolean alwaysShowSensitiveData,
                                                                       boolean alwaysOpenSpoiler) {
        return new NotificationViewData.Concrete(
                notification.getType(),
                notification.getId(),
                notification.getAccount(),
                statusToViewData(
                        notification.getStatus(),
                        alwaysShowSensitiveData,
                        alwaysOpenSpoiler
                ),
                notification.getEmoji(),
                notification.getTarget()
        );
    }

    public static ChatMessageViewData.Concrete chatMessageToViewData(@Nullable ChatMessage msg) {
        if(msg == null) return null;

        return new ChatMessageViewData.Concrete(
                msg.getId(),
                msg.getContent(),
                msg.getChatId(),
                msg.getAccountId(),
                msg.getCreatedAt(),
                msg.getAttachment(),
                msg.getEmojis(),
                msg.getCard()
        );
    }

    @NonNull
    public static ChatViewData.Concrete chatToViewData(Chat chat) {
        return new ChatViewData.Concrete(
                chat.getAccount(),
                chat.getId(),
                chat.getUnread(),
                chatMessageToViewData(
                        chat.getLastMessage()
                ),
                chat.getUpdatedAt()
        );
    }
}