package com.jarvisv2.android;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final int BG = Color.rgb(5, 10, 18);
    private static final int PANEL = Color.rgb(12, 22, 35);
    private static final int CYAN = Color.rgb(0, 229, 255);
    private static final int WHITE = Color.rgb(231, 241, 250);
    private static final int MUTED = Color.rgb(136, 157, 177);
    private LinearLayout root, messages;
    private ScrollView chatScroll;
    private EditText input;
    private SharedPreferences prefs;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(BG);
        prefs = getSharedPreferences("jarvis_local", MODE_PRIVATE);
        buildUi();
        loadMessages();
    }

    private GradientDrawable shape(int color, int stroke, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(radius);
        if (stroke != 0) d.setStroke(1, stroke);
        return d;
    }
    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value); t.setTextSize(size); t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }
    private LinearLayout column() {
        LinearLayout l = new LinearLayout(this); l.setOrientation(LinearLayout.VERTICAL); return l;
    }
    private void buildUi() {
        root = column(); root.setBackgroundColor(BG); setContentView(root);
        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL); header.setOrientation(LinearLayout.HORIZONTAL);
        header.setPadding(20,18,20,16);
        TextView logo = text("J",28,CYAN,true); logo.setGravity(Gravity.CENTER);
        logo.setBackground(shape(PANEL,CYAN,18)); header.addView(logo,new LinearLayout.LayoutParams(52,52));
        LinearLayout titles = column(); titles.setPadding(12,0,0,0);
        titles.addView(text("J.A.R.V.I.S.",21,WHITE,true));
        titles.addView(text("ASSISTENTE PESSOAL / V2",10,CYAN,true));
        header.addView(titles,new LinearLayout.LayoutParams(0,-2,1));
        TextView status = text("● LOCAL",11,CYAN,true); status.setBackground(shape(PANEL,Color.rgb(0,95,110),24));
        status.setPadding(10,8,10,8); header.addView(status); root.addView(header);

        ScrollView page = new ScrollView(this);
        LinearLayout content = column(); content.setPadding(18,4,18,22); page.addView(content);
        root.addView(page,new LinearLayout.LayoutParams(-1,0,1));
        content.addView(text("Sistema iniciado.",24,WHITE,true));
        TextView subtitle = text("Seu centro de comando pessoal.",14,MUTED,false);
        subtitle.setPadding(0,5,0,18); content.addView(subtitle);

        LinearLayout statusCard = column(); statusCard.setBackground(shape(PANEL,Color.rgb(22,53,70),18));
        statusCard.setPadding(16,15,16,15); statusCard.addView(text("STATUS DO SISTEMA",11,CYAN,true));
        TextView statusBody = text("Interface Android pronta\nMemória local: ativa\nIA Gemma: aguardando integração",14,WHITE,false);
        statusBody.setPadding(0,9,0,0); statusCard.addView(statusBody); content.addView(statusCard);

        TextView businessTitle = text("PAINEL DE NEGÓCIOS",13,CYAN,true);
        businessTitle.setPadding(0,24,0,10); content.addView(businessTitle);
        addBusinessCard(content,"ACHADINHOS","Projeto de afiliados","Organizar produtos, ofertas e próximos passos.");
        addBusinessCard(content,"META FINANCEIRA","Objetivo: R$ 1.000.000","Acompanhar receitas, despesas e evolução.");
        addBusinessCard(content,"PROJETOS","Central de execução","Planejar tarefas e transformar ideias em ações.");

        TextView assistantTitle = text("ASSISTENTE",13,CYAN,true);
        assistantTitle.setPadding(0,24,0,10); content.addView(assistantTitle);
        TextView note = text("Esta versão ainda não tem um modelo de IA conectado. As mensagens ficam salvas neste aparelho; nenhuma API externa é usada.",12,MUTED,false);
        note.setBackground(shape(PANEL,0,14)); note.setPadding(14,12,14,12); content.addView(note);
        chatScroll = new ScrollView(this); messages = column(); messages.setPadding(0,10,0,10); chatScroll.addView(messages);
        LinearLayout.LayoutParams chatParams = new LinearLayout.LayoutParams(-1,260); chatParams.topMargin=4; content.addView(chatScroll,chatParams);

        LinearLayout composer = new LinearLayout(this); composer.setOrientation(LinearLayout.HORIZONTAL);
        composer.setGravity(Gravity.CENTER_VERTICAL); composer.setPadding(12,10,12,10); composer.setBackground(shape(PANEL,Color.rgb(25,51,68),18));
        input = new EditText(this); input.setSingleLine(true); input.setTextColor(WHITE); input.setHintTextColor(MUTED);
        input.setHint("Escreva uma mensagem..."); input.setTextSize(14); input.setBackgroundColor(Color.TRANSPARENT);
        composer.addView(input,new LinearLayout.LayoutParams(0,48,1));
        Button send = new Button(this); send.setText("ENVIAR"); send.setTextSize(11); send.setTextColor(BG); send.setAllCaps(false); send.setBackground(shape(CYAN,0,12));
        composer.addView(send,new LinearLayout.LayoutParams(-2,46)); send.setOnClickListener(v -> sendMessage()); root.addView(composer);
        TextView footer = text("JARVIS V2 • PROTÓTIPO OFFLINE",9,MUTED,true); footer.setGravity(Gravity.CENTER);
        footer.setPadding(0,7,0,8); root.addView(footer);
    }
    private void addBusinessCard(LinearLayout parent,String tag,String title,String detail) {
        LinearLayout card = column(); card.setBackground(shape(PANEL,Color.rgb(23,43,58),16)); card.setPadding(15,14,15,14);
        card.addView(text(tag,10,CYAN,true)); TextView h=text(title,17,WHITE,true); h.setPadding(0,5,0,4); card.addView(h);
        card.addView(text(detail,12,MUTED,false)); LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2); lp.bottomMargin=9; parent.addView(card,lp);
    }
    private void loadMessages() {
        String saved=prefs.getString("messages","");
        if(saved.isEmpty()) { addBubble("JARVIS","Interface pronta. O modelo de IA local ainda precisa ser integrado.",false,false); return; }
        for(String row:saved.split("\\n")) { int sep=row.indexOf("|"); if(sep>0) addBubble(row.substring(0,sep),row.substring(sep+1),row.startsWith("VOCÊ|"),false); }
    }
    private void sendMessage() {
        String value=input.getText().toString().trim(); if(value.isEmpty()) return;
        addBubble("VOCÊ",value,true,true); input.setText("");
        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(input.getWindowToken(),0);
        addBubble("JARVIS","Recebi sua mensagem e a salvei localmente. Para responder de forma inteligente e offline, falta integrar o modelo Gemma a este aplicativo.",false,true);
    }
    private void addBubble(String who,String message,boolean user,boolean persist) {
        LinearLayout bubble=column(); bubble.setBackground(shape(user?Color.rgb(0,53,66):PANEL,user?Color.rgb(0,115,135):Color.rgb(26,47,62),14));
        bubble.setPadding(12,10,12,10); bubble.addView(text(who,10,user?CYAN:MUTED,true));
        TextView body=text(message,13,WHITE,false); body.setPadding(0,4,0,0); bubble.addView(body);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2); lp.bottomMargin=8; messages.addView(bubble,lp);
        chatScroll.post(()->chatScroll.fullScroll(View.FOCUS_DOWN));
        if(persist) { String old=prefs.getString("messages",""); String line=who+"|"+message.replace("\n"," "); prefs.edit().putString("messages",old.isEmpty()?line:old+"\n"+line).apply(); }
    }
}
