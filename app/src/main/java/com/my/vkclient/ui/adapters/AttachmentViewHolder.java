package com.my.vkclient.ui.adapters;

import android.content.Context;
import android.view.View;

import com.my.vkclient.entities.Attachment;

class AttachmentViewHolder extends BaseAttachmentViewHolder<Attachment> {

    AttachmentViewHolder(Context context, View itemView) {
        super(context, itemView);
    }

    @Override
    public void bind(Attachment data) {
        super.bindAttachment(data);
    }
}
