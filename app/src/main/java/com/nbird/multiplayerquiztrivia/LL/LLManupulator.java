package com.nbird.multiplayerquiztrivia.LL;

import androidx.cardview.widget.CardView;

public class LLManupulator {

    CardView audienceLL,expertAdviceLL,fiftyfiftyLL,swapTheQuestionLL;

    public LLManupulator(CardView audienceLL, CardView expertAdviceLL, CardView fiftyfiftyLL, CardView swapTheQuestionLL) {
        this.audienceLL = audienceLL;
        this.expertAdviceLL = expertAdviceLL;
        this.fiftyfiftyLL = fiftyfiftyLL;
        this.swapTheQuestionLL = swapTheQuestionLL;
    }

    public void False(){        //Setting all the lifelines enable:False when an option is selected.
        audienceLL.setClickable(false);
        audienceLL.setEnabled(false);
        audienceLL.setAlpha(0.8f);

        expertAdviceLL.setClickable(false);
        expertAdviceLL.setEnabled(false);
        expertAdviceLL.setAlpha(0.8f);

        fiftyfiftyLL.setClickable(false);
        fiftyfiftyLL.setEnabled(false);
        fiftyfiftyLL.setAlpha(0.8f);

        swapTheQuestionLL.setClickable(false);
        swapTheQuestionLL.setEnabled(false);
        swapTheQuestionLL.setAlpha(0.8f);

    }

    public void True(){       //Setting all the lifelines enable:False when next button is pressed.
        audienceLL.setClickable(true);
        audienceLL.setEnabled(true);
        audienceLL.setAlpha(1.0f);

        expertAdviceLL.setClickable(true);
        expertAdviceLL.setEnabled(true);
        expertAdviceLL.setAlpha(1.0f);

        fiftyfiftyLL.setClickable(true);
        fiftyfiftyLL.setEnabled(true);
        fiftyfiftyLL.setAlpha(1.0f);

        swapTheQuestionLL.setClickable(true);
        swapTheQuestionLL.setEnabled(true);
        swapTheQuestionLL.setAlpha(1.0f);
    }


}
