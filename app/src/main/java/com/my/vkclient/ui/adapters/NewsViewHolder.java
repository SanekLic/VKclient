package com.my.vkclient.ui.adapters;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.vkclient.Constants;
import com.my.vkclient.utils.ImageLoader;
import com.my.vkclient.R;
import com.my.vkclient.utils.ResultCallback;
import com.my.vkclient.Repository.VkRepository;
import com.my.vkclient.entities.Attachment;
import com.my.vkclient.entities.Group;
import com.my.vkclient.entities.News;
import com.my.vkclient.entities.Size;
import com.my.vkclient.entities.User;
import com.my.vkclient.entities.Video;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class NewsViewHolder extends RecyclerView.ViewHolder {
    private final TextView likesTextView;
    private final TextView commentsTextView;
    private final TextView repostsTextView;
    private final TextView viewsTextView;
    private final ImageView sourceIconImageView;
    private final TextView sourceNameTextView;
    private final TextView sourceNewsDateTextView;
    private final ImageView fromIconImageView;
    private final TextView fromNameTextView;
    private final TextView fromNewsDateTextView;
    private final TextView newsTextView;
    private final ImageView newsPhotoImageView;
    private final RecyclerView attachmentRecyclerView;
    private List<Attachment> attachments = new ArrayList<>();
    private Handler mainLooperHandler = new Handler(Looper.getMainLooper());
    private Context context;
    private AttachmentRecyclerViewAdapter attachmentRecyclerViewAdapter;

    public NewsViewHolder(Context context, View itemView) {
        super(itemView);

        this.context = context;
        sourceIconImageView = itemView.findViewById(R.id.sourceIconImageView);
        sourceNameTextView = itemView.findViewById(R.id.sourceNameTextView);
        sourceNewsDateTextView = itemView.findViewById(R.id.sourceNewsDateTextView);
        fromIconImageView = itemView.findViewById(R.id.fromIconImageView);
        fromNameTextView = itemView.findViewById(R.id.fromNameTextView);
        fromNewsDateTextView = itemView.findViewById(R.id.fromNewsDateTextView);
        newsTextView = itemView.findViewById(R.id.newsTextView);
        newsPhotoImageView = itemView.findViewById(R.id.newsPhotoImageView);
        attachmentRecyclerView = itemView.findViewById(R.id.attachmentRecyclerView);
        likesTextView = itemView.findViewById(R.id.likesTextView);
        commentsTextView = itemView.findViewById(R.id.commentsTextView);
        repostsTextView = itemView.findViewById(R.id.repostsTextView);
        viewsTextView = itemView.findViewById(R.id.viewsTextView);

        setupAttachmentRecyclerView();
    }

    public void bind(News news) {
        sourceNewsDateTextView.setText(news.getDate().toString());

        if (news.getSourceId() < 0) {
            VkRepository.getGroupById(news.getSourceId() * (-1), new ResultCallback<Group>() {
                @Override
                public void onResult(final Group result) {
                    mainLooperHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (result != null) {
                                sourceNameTextView.setText(result.getName());
                                sourceIconImageView.setTag(R.id.IMAGE_TAG_IS_CIRCULAR, true);
                                ImageLoader.getImageFromUrl(sourceIconImageView, result.getPhoto100(), 0, 0);
                            }
                        }
                    });
                }
            });
        } else {
            VkRepository.getUserById(news.getSourceId(), new ResultCallback<User>() {
                @Override
                public void onResult(final User result) {
                    mainLooperHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (result != null) {
                                sourceNameTextView.setText(new StringBuilder().append(result.getFirstName()).append(Constants.STRING_SPACE).append(result.getLastName()).toString());
                                sourceIconImageView.setTag(R.id.IMAGE_TAG_IS_CIRCULAR, true);
                                ImageLoader.getImageFromUrl(sourceIconImageView, result.getPhoto100Url(), 0, 0);
                            }
                        }
                    });
                }
            });
        }

        if (news.getCopyHistory() != null) {
            News newsCopy = news.getCopyHistory().get(news.getCopyHistory().size() - 1);
            fromNewsDateTextView.setText(newsCopy.getDate().toString());

            if (newsCopy.getFromId() < 0) {
                VkRepository.getGroupById(newsCopy.getFromId() * (-1), new ResultCallback<Group>() {
                    @Override
                    public void onResult(final Group result) {
                        mainLooperHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (result != null) {
                                    fromNameTextView.setText(result.getName());
                                    fromIconImageView.setTag(R.id.IMAGE_TAG_IS_CIRCULAR, true);
                                    ImageLoader.getImageFromUrl(fromIconImageView, result.getPhoto100(), 0, 0);
                                }
                            }
                        });
                    }
                });
            } else {
                VkRepository.getUserById(newsCopy.getFromId(), new ResultCallback<User>() {
                    @Override
                    public void onResult(final User result) {
                        mainLooperHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (result != null) {
                                    fromNameTextView.setText(new StringBuilder().append(result.getFirstName()).append(Constants.STRING_SPACE).append(result.getLastName()).toString());
                                    fromIconImageView.setTag(R.id.IMAGE_TAG_IS_CIRCULAR, true);
                                    ImageLoader.getImageFromUrl(fromIconImageView, result.getPhoto100Url(), 0, 0);
                                }
                            }
                        });
                    }
                });
            }

            setNewsText(newsCopy.getText());
            setAttachments(newsCopy.getAttachments());
            setVisibilityCopyNews(View.VISIBLE);
        } else {
            setNewsText(news.getText());
            setAttachments(news.getAttachments());
            setVisibilityCopyNews(View.GONE);
        }

        likesTextView.setText(formatNumber(news.getLikes().getCount()));
        commentsTextView.setText(formatNumber(news.getComments().getCount()));
        repostsTextView.setText(formatNumber(news.getReposts().getCount()));
        viewsTextView.setText(formatNumber(news.getViews().getCount()));
    }

    private String formatNumber(int number) {
        if (number == 0) {
            return Constants.STRING_EMPTY;
        }

        if (number < Constants.INT_THOUSAND) {
            return String.valueOf(number);
        }

        int exp = (int) (Math.log(number) / Math.log(Constants.INT_THOUSAND));

        return String.format(Locale.US, Constants.STRING_NUMBER_FORMAT, number / Math.pow(Constants.INT_THOUSAND, exp), Constants.STRING_NUMBER_POSTFIX.charAt(exp - 1));
    }

    private void setNewsText(String text) {
        newsTextView.setText(text);
        if (text == null || text.isEmpty()) {
            if (newsTextView.getVisibility() != View.GONE) {
                newsTextView.setVisibility(View.GONE);
            }
        } else {
            if (newsTextView.getVisibility() != View.VISIBLE) {
                newsTextView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setAttachments(List<Attachment> newAttachments) {
        newsPhotoImageView.setImageDrawable(null);
        attachments.clear();

        if (newAttachments != null) {
            attachments.addAll(newAttachments);

            for (int i = 0; i < attachments.size(); i++) {
                Attachment attachment = attachments.get(i);

                if (Attachment.Type.Photo.equals(attachment.getType())) {
                    setMaxSizePhotoToImageView(attachment.getPhoto().getSizes());
                    attachments.remove(i);

                    break;
                }

                if (Attachment.Type.Doc.equals(attachment.getType())) {
                    setMaxSizePhotoToImageView(attachment.getDoc().getPreview().getPhoto().getSizes());
                    attachments.remove(i);

                    break;
                }

                if (Attachment.Type.Video.equals(attachment.getType())) {
                    setMaxSizePhotoToImageView(attachment.getVideo());
                    attachments.remove(i);

                    break;
                }

                if (Attachment.Type.Link.equals(attachment.getType()) && attachment.getLink().getPhoto() != null) {
                    setMaxSizePhotoToImageView(attachment.getLink().getPhoto().getSizes());
                    attachments.remove(i);

                    break;
                }

                if (Attachment.Type.Podcast.equals(attachment.getType())) {
                    setMaxSizePhotoToImageView(attachment.getPodcast().getPodcastInfo().getCover().getSizes());
                    attachments.remove(i);

                    break;
                }
            }
        }

        attachmentRecyclerViewAdapter.setItems(attachments);
    }

    private void setMaxSizePhotoToImageView(Video video) {
        String urlPhoto = video.getPhoto320Url();
        int width = 320;
        int height = 240;

        if (video.getPhoto800Url() != null) {
            urlPhoto = video.getPhoto800Url();
            width = 800;
            height = 450;
        } else if (video.getPhoto640Url() != null) {
            urlPhoto = video.getPhoto640Url();
            width = 640;
            height = 480;
        }

        ImageLoader.getImageFromUrl(newsPhotoImageView, urlPhoto, width, height);
    }

    private void setMaxSizePhotoToImageView(List<Size> photoSizes) {
        Size showSize = photoSizes.get(0);

        for (Size size : photoSizes) {
            if (size.getWidth() > showSize.getWidth()) {
                showSize = size;
            }
        }

        String urlPhoto = showSize.getUrl() != null ? showSize.getUrl() : showSize.getSrc();
        ImageLoader.getImageFromUrl(newsPhotoImageView, urlPhoto, showSize.getWidth(), showSize.getHeight());
    }

    private void setVisibilityCopyNews(int visible) {
        if (fromIconImageView.getVisibility() != visible) {
            fromIconImageView.setVisibility(visible);
            fromNameTextView.setVisibility(visible);
            fromNewsDateTextView.setVisibility(visible);
        }
    }

    private void setupAttachmentRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        attachmentRecyclerView.setLayoutManager(linearLayoutManager);
        attachmentRecyclerViewAdapter = new AttachmentRecyclerViewAdapter();
        attachmentRecyclerView.setAdapter(attachmentRecyclerViewAdapter);
    }
}
