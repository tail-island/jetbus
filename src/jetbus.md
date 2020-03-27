「Androidアプリの開発は、地球が砕け散ってもイヤです」

上司の問い掛けにノータイムで断りをいれて、でもしがないサラリーマンの私の我儘が通るはずなんかなくて、Androidアプリの開発があまりに辛くて滂沱のごとく涙したあの日の私。私の立場は当時と同じヒラのままですし、やっぱり上司の命令には逆らえないのですけど、Androidアプリの開発は大きく変わりました。

「Androidアプリの開発を、ぜひワタクシメに！」

今の私は、Androidアプリの開発の仕事大歓迎です。だって、今時のAndroidアプリ開発って、とても楽チンで美味しい仕事なんだもの。そう、Android Jetpackを使うならね。あと、Kotlinとコルーチンも。……え？　信じられない？　わかりました。実際にアプリを作って、その楽チンさを証明しましょう。

# Androidアプリ開発が面倒だった理由と、公式による対策

さて、Androidアプリの開発が面倒なのは、大昔の貧弱なスマートフォンのリソースでも動作するように初期のバージョンが作成されたためだと考えます。そして、複数のメーカーの様々なAndroid端末があったので後方互換性を考慮しなければならなくて、初期バージョンの古い設計を引きずらざるを得なかったためでしょう。

たとえば、Androidアプリの基本的な構成単位である`Activity`は、いつ終了させられてもよいように作らなければなりません。少ないメモリを有効活用するためには、バックグラウンドの`Activity`を終了させてメモリを解放させることが重要ですからね。でも、だからといって、ただ終了させるのでは次にアプリがフォアグラウンドになった時に処理を継続できなくなってしまいます。だから、`onSavedInstanceState()`で状態を退避できて`onRestoreInstanceState()`で状態を復帰できるようにしよう。待てよ、これが前提なのだから、端末を縦から横に回転したときに画面のレイアウトをやり直すようなCPUを消費する処理はやめて、`Activity`を再生成してしまうことにしよう。たぶんこのような流れで出来上がったのが、`Activity`のライフサイクルという、あの複雑怪奇な図です。

図

この面倒な図を正確に理解していないと、Androidアプリの開発はできないらしい。でも、メモリが豊富な今時のスマートフォンなら、こんな仕組みは無駄なのではないでしょうか？　LinuxでもMac OSでもWindowsでも、こんな考慮はしていませんよね？

でも、後方互換性を考えると、APIを丸ごと書き換えるのは困難です。だから、APIの上にライブラリの層を被せることにしましょう。先ほどの問題は`Activity`そのものではなく、`Activity`の「状態」の問題なので、状態だけを抽出して、その状態については`Activity`が破棄されても破棄されないようにすればよい。そのクラスの名前はそう、MVVM（Model-View-ViewModel）アーキテクチャの`ViewModel`から借りてこよう。そして、プログラミング業界ではオブジェクトの生成から破棄までをライフサイクルという言葉で表現するので、ライブラリ名はLifecycleとしよう。ほら、これで、あの複雑怪奇なライフサイクルを（それほど）意識しないで済むようになって、開発は楽チンになりました。

でもちょっと待って。画面回転の問題は`Fragment`で解決できるのではという、従来のAndroid開発に詳しい方がいらっしゃるかもしれません。でもね、`Fragment`を使う時って、画面遷移はどうしていました？　Androidそのものは`Activity`が遷移していくように作られていて、だから`Fragment`を含む`Activity`が遷移していくように作ることになって、結果として`Activity`と`Fragment`にコードが分散して見通しが悪くなるだけになっていませんでしたか？　この問題は`Fragment`が遷移する仕組みで解消できるわけで、その仕組みをシンプルなコードで実現可能にしてくれるライブラリがNavigationです。これから先の人生では`Fragment`のことだけを考えて生きていけるというわけ。気難しいActivityさんと離れられて、さらに楽チンです。

データの管理も大変でした。RDBMS（Relational Database Management System）のSQLite3をAndroidシステムに組み込んでくれたのはとてもありがたいのですけど、そのアクセスには素のSQLを使えってのはいただけません。オブジェクト指向のObjectとRDBMSのRelationをマップする、O/Rマッパーが欲しい。ならば与えようってことでRoomという公式のO/Rマッパーが提供されて、ライブラリ選択に頭を悩ます必要がなくなって楽チン。

本項では紹介しませんけど、これ以外にも便利機能はいっぱいあって、Googleは[Android Jetpack](https://developer.android.com/jetpack)としてまとめて提供しています。バックグラウンド・ジョブ管理のWorkManagerとか、面倒でしょうがなかったカメラ制御を楽にしてくれるCameraXなんてのもあります。新機能をなかなか取り入れられない、過去に縛られたダサいAPIとはこれでオサラバできるんです。

あと、Support Libraryも。Androidでは端末が最新OSにバージョン・アップされていることを期待できないので、OSそのものに新しい機能が組み入れられてもすぐに使うことはできません。だから、Support Libraryという形で、OSとは別に機能が提供されてきました。ところが、このSupport Libraryにもバージョンの不整合という問題がでてしまったんです。`android.support.v4`とか`android.support.v7`とかね。これはもう心機一転してやりなおそうってことで、`androidx`で始まる新しいライブラリ群にまとめなおされました。前述したLifecycleやNavigationも、実は`androidx`ファミリーの一員なんです。Lifecycleは`androidx.lifecycle`ですし、Navigationは`androidx.navigation`です。当然、新しい`androidx.*`では、LifecycleやNavigationなどのJetpackの機能を前提にした機能強化がなされていて、これでもう本当に楽チンです。

ただね、`androidx.*`は、それぞれがそれぞれを前提としているので、整合性を保って良い感じに組み合わせて使わなければならないんですよ。個々の要素を理解するだけでは不十分で、どのように組み合わせるべきかを理解することが重要なんです。だから、Googleは[アプリのアーキテクチャ・ガイド](https://developer.android.com/jetpack/docs/guide?hl=ja)というアプリ全体をどのように作るのかのガイドと、[Sunflower](https://github.com/googlesamples/android-sunflower)というサンプル・アプリを提供しています。これらはとても良いモノなのですけど、私には、Jetpackに都合が良い美しい世界にとどまっていて、現実の様々な問題への対応が不十分なように感じられます。あと、いくらなんでも読者への前提が厳しすぎますよ、これ。ガイドの序文に「Androidフレームワークの基本について熟知していることを前提としています」って書いてありますけど、Jetpack以降は無駄になるような古い知識をとりあえず習得してこいってのは酷いと思うよね？

というわけで、もう少し現実的なサンプル・アプリと、ゆるめのガイドを作成してみました。……思いっきり蛇足な気がするけど、気のせいだよね？

# サンプル・アプリ

私が欲しいからという理由で、東京の都営バスの車両の接近情報を表示するアプリを作成します（私が生まれ育った群馬県で力合わせる200万人をはじめとする地方在住のみなさま、ごめんなさい）。というのもですね、都営バスは[tobus.jp](https://tobus.jp/)というサイトで車両接近情報を提供していて、このサイトはグラフィカルでとてもよくできているんですけど、提供側の都営バスの論理からか情報が路線単位なんですよ。で、私が住んでいるアパートの周りにはいくつかのバス停があってそれぞれ路線が異なるので、会社から帰るときには複数の路線の車両接近情報を調べなければならなくてこれがとにかくかったるい。

図

なので、複数の路線の車両の接近情報を集めて表示するアプリを作成します。タイミングが良いことに、2019年5月31日に、[都営交通の運行情報等をオープンデータとして提供開始します](https://metro.tokyo.jp.tosei/hodohappyo/press/2019/05/31/04.html)というニュースがありました。[公共交通オープンデータセンター開発者サイト](https://developer.odpt.org)でユーザー登録すれば、誰でも無料でデータを使用できます（利用者への通知が必要などの、いくつかの制約はありますけど）。Webサイトのクローリングが不要なのでアプリ開発は容易だろうから、サンプル・アプリに適しているんじゃないかな。

具体化しましょう。出発バス停を指定して、到着バス停を指定すると、それらのバス停を結ぶ全ての路線の接近情報が一覧表示されます。あと、毎回出発バス停と到着バス停を選ぶのでは面倒すぎるので、ブックマークの機能を持たせます。以下のような感じ。

図

本ガイドでは、このアプリをちゃちゃっと作っていきます。

ソース・コードは[GitHub](https://github.com/tail-island/jetbus/)で公開（章単位にブランチを分けるというサービス付き）しているので、必要に応じて参照してみてください。ただし、このコードをビルドして動かすには、後の章で説明する[公共交通オープンデータセンター開発者サイト](https://developer.odpt.org)にユーザー登録すると貰えるアクセス・トークンをが必要です。コードをcloneしたら./app/src/main/res/valuesディレクトリにodpt.xmlを作成し、以下を参考に`consumerKey`を設定してください。

~~~ xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="consumerKey">公共交通オープンデータセンターから取得したアクセス・トークン</string>
</resources>
~~~

あと、Android Studioのバージョンは3.6を使用しました。これより古い場合は、Android Studioをバージョンアップしてください。

# プロジェクトの作成

長い前置きがやっと終わって早速プログラミング……の前に、Androidアプリの開発ではプロジェクトを作成しなければなりません。Android Studioを起動して、プロジェクトを作成しましょう。

Android Studioを起動して、[Start a new Android Studio project]を選択します。

図

作成するプロジェクトは、余計なコードが生成されない「Empty Activity」にします。

図

[Name]にアプリ名、[Package name]にドメイン名＋アプリ名を入力します。アプリ名は、JetpackのサンプルでBusの接近情報ということで、jetbusにしてみました。あ、英語ダメダメで風呂のbathと乗り物のbusの区別がつかなかったわけじゃなくて、イタリーのミラノのJetbus社のファンなだけですよ（あと、jet bathが和製英語なのだということを今Webで調べて知って、大きなダメージを受けました……）。[Language]はもちろん「Kotlin」で。Javaの256倍くらい良いプログラミング言語ですし、Google I/O 2019で「Kotlinファースト」が表明されましたし、Kotlinならさらに有効活用できるJetpackの機能もあるためです。あと、[Minimum API Level]は、「API 23: Android 6.0 (Mashmallow)」にしました。Android 6.0で権限確認の方法が変更になったので、これ以前のバージョンだと権限の確認の処理が面倒なためです（今回は関係ないけどね）。古い端末を平気で使う海外までを対象にしたアプリを作るならもっと古いバージョンにすべきでしょうけど、国内が対象なら、まぁ大丈夫じゃないかな。

図

ともあれ、これで無事にプロジェクトが作成されました。でも、まだJetpackが組み込まれていません。さっそく組み込む……前に、ビルド・システムの説明をさせてください。

## ビルド・システム

人類がウホウホ言いながら樹上で暮らしていた大昔、ライブラリの組み込みというのはインターネットからダウンロードしたファイルをプロジェクトのディレクトリにセーブするという作業でした。Windowsでアプリケーションをインストールするために、Webサイトからパッケージをダウンロードしてセットアップするのと似た感じ。

こんな面倒な作業はやってられませんから、Linuxでは`pacman`とか`apt`とか`yum`とか、Mac OSでは`MacPorts`とか`HomeBrew`とかのパッケージ管理システムを使って、アプリケーションをセットアップします。たとえば、LinuxディストリビューションのArch Linuxで今私が本稿の作成に使用しているEmacsをセットアップする場合は、ターミナルから`sudo pacman -S emacs`と入力するだけで終わり。これだけで、パッケージ管理システムが全自動でEmacsをダウンロードし、セットアップしてくれます（私は日本語IMEにMozcを使用しているので、emacs-mozcからEmacsをインストールしたけど）。AndroidのPlay Store、iPhoneのApp Storeと同じですな。

で、今時の開発では様々なライブラリを使用するのが当たり前で、ライブラリ毎にWebサイトを開いてダウンロードして解凍してセーブなんて作業はやってられませんから、ライブラリの組み込みにも自動化が必要でしょう。ライブラリは他のライブラリに依存していることが多くて、その依存関係を辿る作業を手動でやるなんてのは非現実的ですもんね。

あと、今時の開発だと、ライブラリを組み込み終わったあとのビルドもなかなかに複雑な作業となります。ビルドついでにテストしたいとか、事前にコード生成させたいとか。このように考えると、ライブラリの管理とビルドを自動化するシステムが必要で、これがいわゆるビルド・システムとなります。Android Studioは、このビルド・システムとしてGradleというソフトウェアを使用しているわけです。

## Jetpackの組み込み

Jetpackをビルド・システムのGradleに組み込む方法は、各ライブラリのリリース・ノートに書いてあります。たとえばNavigationなら、[https://developer.android.com/jetpack/androidx/releases/navigation](https://developer.android.com/jetpack/androidx/releases/navigation)です。基本はこのリリース・ノートの記載に従うのですけど、いくつか注意点があります。

* ライブラリ名-ktxという名前のライブラリがある場合は、\*-ktxを指定してください。\*-ktxは、Kotlinならではの便利機能が入ったバージョンのライブラリです。Kotlin向けの機能が入っていない基本バージョンのライブラリは、依存関係があるので自動で組み込まれます。
* Jetpackではコード生成を多用しているのですけど、KotlinやJavaではコード生成の制御にアノテーション（annotation。クラスやメソッドの前に書く`@Foo`みたいなアレ）を使用していて、Javaの場合はGradleのビルド・スクリプトに`annotationProcessor`と書きます。Kotlinの場合は、Kotlin Annotation Processor Toolの略で`kapt`と書いてください。リリース・ノートに`annotationProcessor`の記述があった後に、Kotlinではkaptを使ってねという知っている人しかわからない意味不明なコメントが書いてある場合は、`kapt`の出番です。

と、以上の注意を踏まえて、Android Studioの[Project]ビューのbuild.gradeをダブル・クリックして開いて、修正してみましょう。

図

で、以下が修正した結果。修正した行は、修正内容をコメントで書いています。

~~~ gradle:build.gradle(Project:jetbus)
buildscript {
    ext.kotlin_version = '1.3.61'
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.6.1'
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"

        classpath 'androidx.navigation:navigation-safe-args-gradle-plugin:2.2.1'  // 追加
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
~~~

~~~ gradle:build.gradle(Module:app)
apply plugin: 'com.android.application'
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-android-extensions'
apply plugin: 'kotlin-kapt'                          // 追加
apply plugin: 'androidx.navigation.safeargs.kotlin'  // 追加

android {
    compileSdkVersion 29
    buildToolsVersion "29.0.3"

    defaultConfig {
        applicationId "com.tail_island.jetbus"
        minSdkVersion 23
        targetSdkVersion 29
        versionCode 1
        versionName "1.0"
        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }

    kotlinOptions {        // 追加
        jvmTarget = '1.8'  // 追加
    }                      // 追加

    dataBinding {          // 追加
        enabled true       // 追加
    }                      // 追加
}

dependencies {
    kapt 'androidx.lifecycle:lifecycle-compiler:2.2.0'                  // 追加
    kapt 'androidx.room:room-compiler:2.2.4'                            // 追加

    implementation fileTree(dir: 'libs', include: ['*.jar'])

    implementation 'androidx.appcompat:appcompat:1.1.0'
    implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
    implementation 'androidx.core:core-ktx:1.2.0'
    implementation 'androidx.fragment:fragment-ktx:1.2.2'               // 追加
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'            // 追加
    implementation 'androidx.lifecycle:lifecycle-extensions:2.2.0'      // 追加
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0'   // 追加
    implementation 'androidx.navigation:navigation-fragment-ktx:2.2.1'  // 追加
    implementation 'androidx.navigation:navigation-ui-ktx:2.2.1'        // 追加
    implementation 'androidx.room:room-ktx:2.2.4'                       // 追加
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"

    testImplementation 'junit:junit:4.12'

    androidTestImplementation 'androidx.test.ext:junit:1.1.1'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'
}
~~~

これで、デモ・アプリに必要なJetpackのライブラリの組み込みが完了しました。やっとプログラミングです。最初は、作成してやった感が大きそうな、画面まわりをやってみましょう。

# [Navigation](https://github.com/tail-island/jetbus/tree/navigation)

まぁ、画面といっても、かっこいい画面を作るほうではなく、ダミー画面を使用した画面遷移の実装という、見栄えがあまりよくない部分のプログラミングだけどな。画面*遷移*なので、Navigationが火を吹きますよ。

## `Activity`と`Fragment`とNavigation

さて、例によって歴史の話から。`Acticity`と`Fragment`の話、あと、Navigationが作られた経緯です。

`Activity`ってのは、ぶっちゃけアプリの画面1つ分です。で、一般にアプリは複数の`Activity`で構成されます。Androidはアプリ間連携（アプリを操作していたら別のアプリの画面に遷移して、で、戻るボタンで最初のアプリに戻れる）ができるところがとても素晴らしいと私は思っているのですけど、この機能は`Activity`の遷移として実現されています。同様に、アプリ内でも`Activity`の遷移で画面遷移を実現していました。

で、大昔のスマートフォンのアプリの単純な画面だったらこれであまり問題なかったんですけど、高機能なアプリを作ったりタブレットのような大きな画面を効率よく使おうとしたりする場合は、この方式だとコードの重複という問題が発生してしまうんです。タブレットの大きな画面では、左に一覧表示して、右にその詳細を表示するような画面が考えられます。でも、スマートフォンの小さな画面では、一覧表示する画面と、それとは別の詳細を表示する画面に分かれて、画面遷移する形で表現しなければなりません。

図

これを`Activity`で実現しようとすると、タブレット用の`Activity`を1つと、スマートフォン用の`Acticvity`を2つ作らなければなりません。そして、ほとんどのコードは重複してしまうでしょう。UIの問題なら`View`（UIコンポーネント）で解決すれば……って思うかもしれませんけど、画面に表示するデータをデータベースから取得してくるような機能を`View`に持たせるのは、`View`の責任範囲を逸脱しているのでダメです。

ではどうすればよいかというと、`Activity`の構成要素になりえる「何か」を追加してあげればよい。この「何か」こそが、`Fragment`です。

でもね、コードの重複が発生しないようなケースで`Fragment`を使って`Activity`で画面遷移をさせると、`Activity`のコードの多くを`Fragment`に移して、で、`Activity`に`Fragment`を管理するコードを追加して、そしてもちろん`Fragment`にも自分自身の初期化処理等が必要となって、結局、コード量が増えただけで誰も得しないという状況になってしまうんです。

これでは無意味なので、1つの`Activity`の中で、複数の`Fragment`が遷移するというプログラミング・スタイルが編み出されました。このスタイルを実現するのが`FragmentTransaction`というAPIで、`Acticity`から`Fragment`を削除したり追加したり入れ替えたりできます。なんと[戻る]ボタンへの対応機能付き……なのですけど、高機能な分だけ使い方が複雑で、`Activity`の遷移（`Intent`のインスタンスを引数に`startActivity()`するだけ）と比べると面倒だったんですよ。

なので、Navigationが作成されました。GUIツール（私はほとんど使わないけど）で画面遷移を定義できるかっこいいライブラリです。ただ単に`FragmentTransaction`を呼び出しているだけな気もしますけど、まぁ、いろいろ楽チンなので良し。SafeArgsという便利機能もありますしね。

## プロジェクトにFragmentを追加する

以上により、画面遷移は`Fragment`遷移ということになりました。だから、プロジェクトに`Activity`ではなく`Fragment`を追加しましょう。jetbusに必要な`Fragment`は、以下の5つとなります。

* SplashFragment（起動処理をする間に表示するスプラッシュ画面）
* BookmarksFragment（ブックマークの一覧を表示する画面）
* DepartureBusStopFragment（出発バス停を指定する画面）
* ArrivalBusStopFragment（到着バス停を指定する画面）
* BusApproachesFragment（バスの接近情報を表示する画面）

さっそく追加します。プロジェクトを右クリックして、[New] - [Fragment] - [Fragment (Blank)]メニューを選択してください。

図

`Fragment`の名前を入力して、[Include fragment factory methods?]チェックボックスを外して、[Finish]ボタンを押します（このチェックボックスを外さないと、余計なコードが生成されてしまうためです。まぁ、生成されたとしても、そのコードを消せば良いだけなんですけど）。この手順を5回繰り返して、必要な`Fragment`をすべて生成してください。

図

ふう、完了。

## navigationリソースを作成する

さて、Navigation。Navigationでは、画面の遷移をres/navigationの下のXMLファイルで管理します。このファイルを作りましょう。プロジェクトを右クリックして、[File] - [New] - [Android Resource Directory]メニューを選びます。

図

[Resource type]を「navigation」にして、[OK]ボタンを押してください。これで、navigationディレクトリが生成されます。

次。画面の遷移を管理するXMLファイルです。プロジェクトを右クリックして、[File] - [New] - [Android Resource File]メニューを選びます。

図

[Resource type]を「Navigation」にして、[File name]に「navigation」と入力して、[OK]ボタンを押します。これで、navigation.xmlが生成されました。

図

GUIツールはかったるいという私の個人的な趣味嗜好により、右上の[Code]アイコンをクリックして、以下のXMLファイルを入力します。

~~~ xml
<?xml version="1.0" encoding="utf-8"?>
<navigation
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    app:startDestination="@id/splashFragment">

    <fragment
        android:id="@+id/splashFragment"
        android:name="com.tail_island.jetbus.SplashFragment"
        android:label="@string/app_name"
        tools:layout="@layout/fragment_splash">

        <action
            android:id="@+id/splashFragmentToBookmarksFragment"
            app:destination="@id/bookmarksFragment" />

    </fragment>

    <fragment
        android:id="@+id/bookmarksFragment"
        android:name="com.tail_island.jetbus.BookmarksFragment"
        android:label="ブックマーク"
        tools:layout="@layout/fragment_bookmarks">

        <action
            android:id="@+id/bookmarksFragmentToDepartureBusStopFragment"
            app:destination="@id/departureBusStopFragment" />

        <action
            android:id="@+id/bookmarksFragmentToBusApproachesFragment"
            app:destination="@id/busApproachesFragment" />

    </fragment>

    <fragment
        android:id="@+id/departureBusStopFragment"
        android:name="com.tail_island.jetbus.DepartureBusStopFragment"
        android:label="出発バス停"
        tools:layout="@layout/fragment_departure_bus_stop">

        <action
            android:id="@+id/departureBusStopFragmentToArrivalBusStopFragment"
            app:destination="@id/arrivalBusStopFragment" />

    </fragment>

    <fragment
        android:id="@+id/arrivalBusStopFragment"
        android:name="com.tail_island.jetbus.ArrivalBusStopFragment"
        android:label="到着バス停"
        tools:layout="@layout/fragment_arrival_bus_stop">

        <argument
            android:name="departureBusStopName"
            app:argType="string" />

        <action
            android:id="@+id/arrivalBusStopFragmentToBusApproachesFragment"
            app:destination="@id/busApproachesFragment"
            app:popUpTo="@id/bookmarksFragment" />

    </fragment>

    <fragment
        android:id="@+id/busApproachesFragment"
        android:name="com.tail_island.jetbus.BusApproachesFragment"
        android:label="バス接近情報"
        tools:layout="@layout/fragment_bus_approaches">

        <argument
            android:name="departureBusStopName"
            app:argType="string" />

        <argument
            android:name="arrivalBusStopName"
            app:argType="string" />

    </fragment>

</navigation>
~~~

どのような`Fragment`があるのかは、XMLのタグで表現します。`<fragment>`タグでですね。`android:name`属性で`Fragment`を実装するクラスを、`android:label`属性で画面に表示するタイトルを設定します。`tools:layout`属性は、GUIツールでグラフィカルに表示する場合向けの、プレビュー用のレイアウトの指定です。

`<fragment>`タグの中の`<action>`タグは、画面遷移を表現します。`app:destination`属性は、遷移先を指定します。`arrivalBusStopFragmentToBusApproachesFragment`で指定されている`app:popUpTo`属性は、[戻る]ボタンが押された場合の行き先を指定しています。出発バス停を選んで、到着バス停を選んで、バスの接近情報が表示されたあとに[戻る]ボタンを押す場合は、多分その路線の情報はもういらない場合でしょうから、`app:popUpTo`属性を使用してブックマーク画面まで一気に戻るようにしました。

`<fragment>`の中の`<argument>`は、フラグメントに遷移する際のパラメーターです。選択された出発バス停を使って到着バス停の選択肢を抽出しないと、到着バス停を選ぶところで路線がつながっていないバス停が表示されてしまうでしょ？　だから、`arrivalBusStopFragment`では`departureBusStopName`というパラメーターを指定しました。

あとはそう、`android:id`の説明を忘れていました。AndroidのリソースのXMLでは、`@+id/`の後に続ける形で識別子を設定します。上のXMLの`android:id="@+id/splashFragment"`みたいな感じ。`@+id/`の部分が何だか分からなくて気持ち悪いという方のために補足しておくと、Androidアプリの開発ではR.javaというファイルが内部で自動生成されていて、R.javaの中にリソースの識別子が32bit整数で定義されています。XMLのような文字列の識別子では照合作業でCPUを大量に消費してしまうからという貧乏実装、でも、CPUを消費するということは電池を消費するということで、電池は今でもとても貴重な資源なので今でも素晴らしい実装です。このR.idにIDを追加して適当な32bit整数を割り振っておいてくださいねって指示が、`@+id/`なんです。ちなみに、割り振られた32bit整数を参照してくださいってのは`@id/`です。`<action>`の中の`app:destination="@id/bookmarksFragment"`みたいな感じになります。

## layout

続けて、作成した\<navigation\>を使うように、画面を定義していきましょう。画面遷移をXMLで表現したように、Androidアプリの開発では、画面の構造もXMLで表現します。文字列を表示するなら`<TextView ... />`みたいな感じです。で、この画面定義の際に重要な属性は`androud:layout_width`と`android:layout_height`です。

`android:layout_width`と`android:layout_height`は、UIコンポーネントの幅と高さとなります。ここに指定するのは、具体的な大きさ（`64dp`など）か、`match_parent`、`wrap_content`になります。`match_parent`は階層上の親と同じところまで大きくするようにとの指示、`wrap_content`はコンテンツが入る大きさでお願いしますという指示です。

## ConstraintLayout

あと、AndroidのUIコンポーネントでは、他のコンポーネントを子要素として持てるものを`ViewGroup`、`ViewGroup`を継承して何らかのレイアウト機能を追加したものをLayoutと呼びます。Layoutには、後述するApp barを作るための`AppBarLayout`のような目的に特化したものと、`LinearLayout`(縦や横に直線状に並べる）のような汎用的なものがあります。で、今ここで話題にしたいのは、汎用的なほうのLayout。

貧弱なCPUを考慮した結果だと思う（作るのが面倒だったからだとは思いたくない）のですけど、AndroidのAPIはとても単純な機能のLayoutしか提供しません。直線状に並べる`LinearLayout`とか並べないで重ねる`FrameLayout`みたいなのとか。これらの単純なLayoutの組み合わせで複雑なレイアウトを実現するのがAndroidアプリ開発者の腕の見せ所……だったのは遠い昔の話で、今は、`androidx.constraintlayout.widget.ConstraintLayout`だけを覚えれば大丈夫になりました。

`ConstraintLayout`は、コンポーネントの位置を他のコンポーネントとの関係で表現します。「住所入力欄は名前入力欄の下」みたいな感じですね。具体的には、`app:layout_constraintTop_toBottomOf="@id/nameTextField"`のようになります。左右については少し注意が必要で、アラビア語のように右から左に書く言語にも対応するために、LeftとRightではなくStartとEndで表現します。「住所入力欄のStartは名前入力欄のStartに合わせる」なら、`app:layout_constraintStart_toStartOf="@id/nameTextField"`にするというわけ。

## NavHostFragmentを追加する

これで一般論が終わりました。Projectビューの[app] - [res] - [layout]の下の「activityu\_main.xml」を開いて、[Text]タブを選択して、以下を入力してください。

~~~ xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <fragment
        android:id="@+id/navHostFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:name="androidx.navigation.fragment.NavHostFragment"
        app:navGraph="@navigation/navigation"
        app:defaultNavHost="true" />

</androidx.constraintlayout.widget.ConstraintLayout>
~~~

注目していただきたいのは`<fragment>`タグの`android:name`属性のところ。`android:name`属性で実装のクラスを指定できるのはnavigationのときと同じで、今回のXMLで指定しているのは「androidx.navigation.fragment.NavHostFragment」で、Googleの人が作ったクラスです。このクラスが何なのかリファレンスで調べてみるとNavigationのためのエリアを提供すると書いてありますので、ここにNavigation配下の画面が表示されるようになったわけ。あとは、`app:navGraph`属性で先程作成したnavigation.xmlを指定して、`app:defaultNavHost`属性でデフォルトに指定しておきます。

次は、ダミーの画面レイアウトを作りましょう。そうしておかないと、遷移したのかどうか分からないですもんね。

## layout、再び

さて、これから`Fragment`向けのダミーのlayoutを作成していくわけですけど、その際に、Android Studioが自動で生成したXMLとはルート要素を変更します。Android Studioが生成したXMLではルート要素は具体的な`Layout`（私の環境では`FrameLayout`でした）だったのですけど、これを`<layout>`タグに変更します。

なんでこんなことをするのかというと、後の章で説明するデータ・バインディングをやりたいから。データ・バインディングをやる際には`<data>`タグでデータを指定するのですけど、`<FrameLayout>`タグの下には`<data>`タグを書けませんもんね。あと、ルート要素を`<layout>`タグにしておくと、Bindingクラスが生成されるようになってプログラミングがちょっと楽になるというのがあります。Bindingクラスについては後述することにして、代表例として、fragment_bookmarks.xmlを載せます。

~~~ xml
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    tools:context=".BookmarksFragment">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <TextView
            android:id="@+id/textView"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:layout_marginStart="16dp"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            android:text="Bookmarks" />

        <Button
            android:id="@+id/departureBusStopButton"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            app:layout_constraintTop_toBottomOf="@id/textView"
            app:layout_constraintStart_toStartOf="@id/textView"
            android:text="DEPARTURE BUS STOP" />

        <Button
            android:id="@+id/busApproachesButton"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            app:layout_constraintTop_toBottomOf="@id/departureBusStopButton"
            app:layout_constraintStart_toStartOf="@id/departureBusStopButton"
            android:text="BUS APPROACHES" />

    </androidx.constraintlayout.widget.ConstraintLayout>

</layout>
~~~

ConstraintLayoutのところでUIコンポーネントは`app:layout_constraint...`属性でレイアウトすると説明しておきながら、そこでは使わなかった`app:layout_constraint...`がやっと出てきてくれました（activity\_main.xmlでは、`android:layout_width`属性も`android:layout_height`属性も「match_parent」だったので、制約をつける必要がなかったんですよ）。自分がなんの画面なのかを表示する`TextView`を左上に、その下に`Button`を表示するようになっています。`android:layout_marginTop`属性はマージンです。マージン分だけ隙間を開けてくれるわけですな。
こんな感じでルート要素が`<layout>`になるように、他の`Fragment`のレイアウトも同様に修正してください。

## Fragmentの実装

レイアウトの修正が終わったら、ロジックの修正です。代表例は先ほどと同じ`BookmarksFragment`で。

~~~ kotlin
package com.tail_island.jetbus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tail_island.jetbus.databinding.FragmentBookmarksBinding

class BookmarksFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentBookmarksBinding.inflate(inflater, container, false).apply {
            departureBusStopButton.setOnClickListener {
                findNavController().navigate(BookmarksFragmentDirections.bookmarksFragmentToDepartureBusStopFragment())
            }

            busApproachesButton.setOnClickListener {
                findNavController().navigate(BookmarksFragmentDirections.bookmarksFragmentToBusApproachesFragment("日本ユニシス本社前", "深川第八中学校前"))
            }
        }.root
    }
}
~~~

importの最後で`com.tail_island.jetbus.databinding.FragmentBookmarksBinding`をインポートしていますけど、この`FragmentBookmarksBinding`クラスは、先程fragment_bookmarks.xmlのルート要素を`<layout>`タグに変更したことで生成されたクラスです（XMLのファイル名からクラス名を決定するので、`BookmarksFragmentBinding`ではなくて`FragmentBookmarksBinding`になります。不整合がキモチワルイけど、Android Studioのデフォルトはこのファイル名なのでしょうがない……）。あと、もしコードを書いているときにAndroid Studioがそんなクラスはないよと文句をつけてきたら、レイアウトXMLの変更を忘れたか、レイアウトXMLの変更後にビルドをしていないか（ビルドのときに自動生成されます）です。確認してみてください。

次。`Fragment`を初期化する処理は、コンストラクタではなく、`onCreateView()`メソッドの中に書くことに注意してください。`Fragment`は画面と結び付けられていて、画面というのは複雑で初期化が大変なソフトウェア部品です。だからAndroidでは、初期化のステージに合わせて小刻みにメソッドが呼ばれる方式が採用されました。`Fragment`を作成するときに呼ばれる`onCreate()`メソッドとか、`Fragment`の`View`を生成するときに呼ばれる`onCreateView()`メソッドとか。で、一般に`Fragment`の初期化というのは画面の初期化なので、画面を構成する`View`がない状態では初期化の作業ができません。だから、`onCreate()`メソッドではなくて、`onCreateView()`メソッドに初期化のコードを書きます。

さて、`onCreateView()`メソッドでやらなければならない作業は、`Fragment`の`View`の生成です。Android Studioが生成する`Fragment`のコードでは、引数で渡ってくる`inflater: LayoutInflater`の`inflate()`メソッドをレイアウトXMLを引数にして呼び出すコードが生成されるのですけど、将来のデータ・バインディングのために、`FragmentBookmarksBinding`の`inflate()`メソッドを使用します。で、`...Binding`の`inflate()`メソッドで生成されるのは`...Binding`なので、`View`である`root`プロパティを返します。コードにすると、こんな感じ。

~~~ kotlin
return FragmentBookmarksBinding.inflate(inflater, container, false).root
~~~

## スコープ関数

でもね、私達は画面の初期化をしたいわけで、初期化にはボタンが押されたときの`Listener`の登録等も含まれます。以下のようなコードになるでしょうか。

~~~ kotlin
val binding = FragmentBookmarksBinding.inflate(inflater, container, false)

binding.departureBusStopButton.setOnClickListener {
    // ボタンが押された場合の処理
}

return binding.root
~~~

`...Binding`では、レイアウトXMLで定義されたUIコンポーネントを`android:id`と同じプロパティ名で参照するためのコードが生成されていますから、`binding.departureBusStopButton`で画面のボタンを参照できます。ボタンがクリックされたときのリスナーを設定するメソッドは`setOnClickListener()`で、その引数は関数です。で、関数はラムダ式で定義することができます。あと、Kotlinには、最後のパラメータがラムダ式の場合はそのパラメーターはカッコの外に指定するという慣習があります。さらに、ラムダ式だけを引数にする場合は、括弧を省略できる。というわけで、上のようなシンプルなコードになるわけです。

ただね、このようなコードはよく見るのですけど、実はこれ、とても悪いコードなんです。その理由は、ローカル変数（`val binding`）を使っているから（グローバル変数を使えと言っているわけじゃないですよ、念の為）。

ローカル変数は、そのブロックを抜けるまで有効です。長期間に渡ってコードに影響を与えるというわけ。`val`にしてイミュータブル（不変）にした場合でも、状態遷移という影響は減るけれども変数があることを覚えておかなければならないのは一緒で、コードを読むのが大変になってしまう。私のような記憶力が衰えまくっているおっさんには、ローカル変数は辛いんですよ。だから、変数のスコープを小さくします。関数の最初で変数を宣言しなければならなかくてその変数が関数の最後まで有効な大昔のプログラミング言語より、どこでも変数を宣言できてブロックが終わると変数のスコープが切れる今どきの言語の方が使いやすいですよね？　変数のスコープは、小さければ小さいほど良いんです。

と、このような、変数のスコープを小さく、かつ、分かりやすい形で制御したい場合に使えるKotlinの便利ライブラリが、スコープ関数なんです。

たとえば、`foo()`の戻り値を使用したい場合は、

~~~ kotlin
val x = foo()

x.bar()
~~~

と書くのではなくて、

~~~ kotlin
foo().let { it.bar() }
~~~

と書きます。`let()`スコープ関数は、自分自身を引数にしたラムダ式を呼び出すというわけ。あ、ラムダ式の中の`it`は、ラムダ式のパラメーターを宣言しなかった場合の暗黙の名前です。`let()`スコープ関数は自分自身を引数にしますから、この場合の`it`は`foo()`の戻り値になります。

他のスコープ関数には、`apply()`（`this`を自分自身に設定したラムダ式を呼び出して、自分自身を返す。初期化等で便利）、`also()`（自分自身を引数にしたラムダ式を呼び出して、自分自身を返す。自分自身を使う他のオブジェクトの初期化等で便利）、`run()`（自分自身を`this`にしたラムダ式を実行して、ラムダ式の戻り値を返す。その場で関数を定義して実行感じ）があります。とにかく便利ですから、ぜひ使い倒してください。

というわけで、今回は`apply()`を使用して、`FragmentBookmarksBinding`のボタンへのリスナー登録という初期化処理を書きます。

~~~ kotlin
return FragmentBookmarksBinding.inflate(inflater, container, false).apply {
    departureBusStopButton.setOnClickListener {
        // ボタンが押された場合の処理
    }
}.root
~~~

`apply()`なので`this`は`FragmentBookmarksBinding`になっていて、だから`FragmentBookmarksBinding`の`departureBusStopButton`に修飾なしでアクセスできて便利。スコープの範囲がインデントされて判別しやすいのも、認知機能が衰えた私のようなおっさんには嬉しいです。

## 画面遷移

Navigationでの画面の遷移は、`findNavController()`で取得できる`NavController`クラスの`navigate()`メソッドで実施します。リファレンスを見てみると`navigation()`メソッドにはオーバーロードされた様々なバリエーションがあって、色々な引数を取れるようになっています。今回、この中で注目していただきたいのは、`NavDirections`を引数に取るバージョンです。ナビゲーションのXMLで`<action>`を作成するとこのNavDirectionsが自動生成されるので、とても簡単に呼び出せます。というわけで、画面遷移のコードは以下のようになります。

~~~ kotlin
findNavController().navigate(BookmarksFragmentDirections.bookmarksFragmentToDepartureBusStopFragment())
~~~

あと、記憶力が良い方は、ナビゲーションのXMLを作成したときに、`<argument>`で`Fragment`への遷移のパラメーターを定義したことを覚えているかもしれません。`<argument>`を定義しておくと、`NavDirections`を生成するときにパラメーターを追加してくれます。記憶力が壊滅している私のようなおっさんの場合でも、パラメーターを指定しないとコンパイル・エラーになるから思い出せますな。というわけで、バス接近情報に画面遷移する場合は以下のようなコードになります。

~~~ kotlin
findNavController().navigate(
    BookmarksFragmentDirections.bookmarksFragmentToBusApproachesFragment("日本ユニシス本社前", "深川第八中学校前")
)
~~~

引数は、プログラムを書く以外はあまねくダメダメと評判のこんな私を雇ってくれる奇特な会社から家まで帰るルートですな。

## Navigationを試してみる

こんな感じですべての`Fragment`をプログラミングして、動かしてみると以下のようになります。

動画

[到着バス停]画面から[バス接近情報]画面に遷移した後、[戻る]ボタンを押すとレイアウトのXMLの`app:popUpTo`属性が効いて、[ブックマーク一覧]画面に戻ってくれていてとても嬉しい。

でも、なんか画面が寂しい気がします……。どうにかならないかな？　あと、ナビゲーションのXMLでわざわざ設定した`android:label`属性はどうなったのでしょうか？　どの画面でも、画面上部には「jetbus」って表示されているんだけど。

## App barとNavigation drawer

さて、AndroidアプリのUIは、[Material Design](https://material.io)というデザイン・ガイドに従うことになっています。このMaterial Designにはいろいろなコンポーネントがあるのですけど、App barというコンポーネントで画面の情報を表示して、Navigation drawerでメニューを実現する方式が一般的みたいです。

図

図

今の画面でもApp barっぽいのはありますけど、Navigation drawerがありません。作ってみましょう。[Project]ビューの[app] - [res] - [layout]の下の「activity\_main.xml」を開いて、右上の[Code]アイコンを選択して、activity\_main.xmlを以下に変更します。`Fragment`のレイアウトと統一するために、ルート要素は`<layuout>`に変更しました。

~~~ xml
<?xml version="1.0" encoding="utf-8"?> <layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    tools:context=".MainActivity">

    <androidx.drawerlayout.widget.DrawerLayout
        android:id="@+id/drawerLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <androidx.constraintlayout.widget.ConstraintLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent">

            <com.google.android.material.appbar.AppBarLayout
                android:id="@+id/appBarLayout"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                app:layout_constraintTop_toTopOf="parent">

                <androidx.appcompat.widget.Toolbar
                    android:id="@+id/toolbar"
                    android:layout_width="match_parent"
                    android:layout_height="?attr/actionBarSize" />

            </com.google.android.material.appbar.AppBarLayout>

            <fragment android:id="@+id/navHostFragment"
                android:layout_width="match_parent"
                android:layout_height="0dp"
                app:layout_constraintTop_toBottomOf="@id/appBarLayout"
                app:layout_constraintBottom_toBottomOf="parent"
                android:name="androidx.navigation.fragment.NavHostFragment"
                app:navGraph="@navigation/navigation"
                app:defaultNavHost="true" />

        </androidx.constraintlayout.widget.ConstraintLayout>

        <com.google.android.material.navigation.NavigationView
            android:layout_width="wrap_content"
            android:layout_height="match_parent"
            android:layout_gravity="start"
            app:headerLayout="@layout/item_navigation_header"
            app:menu="@menu/menu_navigation"
            style="@style/Widget.MaterialComponents.NavigationView">

            <androidx.constraintlayout.widget.ConstraintLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent">

                <TextView android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginStart="16dp"
                    android:layout_marginEnd="16dp"
                    android:layout_marginBottom="16dp"
                    app:layout_constraintBottom_toBottomOf="parent"
                    android:textSize="10sp"
                    android:text="本アプリが使用する公共交通データは、公共交通オープンデータセンターにおいて提供されるものです。\n\n公共交通事業者により提供されたデータを元にしていますが、必ずしも正確／完全なものとは限りません。本アプリの表示内容について、公共交通事業者に直接問い合わせないでください。\n\n本アプリに関するお問い合わせはrojima1@gmail.comにお願いします。" />

            </androidx.constraintlayout.widget.ConstraintLayout>

        </com.google.android.material.navigation.NavigationView>

    </androidx.drawerlayout.widget.DrawerLayout>

</layout>
~~~

……長くてごめんなさい。でも、思い出してください。我々はNavigationを使用していますので、画面といえば`Fragment`なわけです。`Activity`はアプリ全体でこれ一つだけ。なので、まぁ、一回だけならば長くても許容できるかなぁと。次のアプリの開発でも、ほぼコピー＆ペーストでいけますしね。私もこれ、コピー＆ペーストで作成して、`<NavigationView>`の子要素の部分を付け加えただけで作っています。本アプリで子要素を追加するという面倒な作業が追加になった理由は、[公共交通オープンデータセンター](https://www.odpt.org/)からデータを取得する際には上のコード中の「本アプリが使用する……」のような通知を書く必要があったためです。

でも、あれ？　`<com.google.android.material.navigation.NavigationView>`タグの属性を見ていくと、`app:headerLayout="@layout/item_navigation_header"`や`app:menu="@menu/menu_navigation"`と書いてあって、こんなリソースは無いとエラーになっています。以下の図のようにNavigation drawerはヘッダーとメニューとそれ以外で構成されていて、それ以外は子要素で定義したので、ヘッダーとメニューを定義しなければならないんですね。

図

というわけで、ヘッダーを作成します。プロジェクトを右クリックして、[New] - [Android Resource File]メニューを選択し、[File name]に「item\_navigation\_header」を入力して[Resource type]を「Layout」に設定し、作成されたitem\_navigation\_header.xmlに以下を入力します。

~~~ xml
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:paddingBottom="16dp"
        android:background="@color/colorPrimary">

        <ImageView
            android:id="@+id/imageView"
            android:layout_width="64dp"
            android:layout_height="64dp"
            android:layout_marginStart="16dp"
            android:layout_marginTop="16dp"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            android:src="@mipmap/ic_launcher_round" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="8dp"
            app:layout_constraintStart_toEndOf="@id/imageView"
            app:layout_constraintTop_toTopOf="@id/imageView"
            app:layout_constraintBottom_toBottomOf="@id/imageView"
            android:text="@string/app_name" />

    </androidx.constraintlayout.widget.ConstraintLayout>

</layout>
~~~

次。メニューです。プロジェクトを右クリックして、[New] - [Android Resource Directory]メニューを選択し、[Resource type]を「Menu」に設定してres/menuを作成します。そのうえで、プロジェクトを右クリックして、[New] - [Android Resource File]メニューを選択して、[File name]に「menu\_navigation」を入力して[Resource type]を「Menu」に設定し、作成されたmenu\_navigation.xmlに以下を入力します。

~~~ xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <group>
        <item android:id="@+id/clearDatabase" android:title="バス停と路線のデータを再取得" />
    </group>
</menu>
~~~

[公共交通オープンデータセンター](https://www.odpt.org/)から取得したデータのキャッシュが古くなった場合に、再取得するためのメニューですね。

いろいろと作業してきましたけど、ごめんなさい、でもまだ終わりません。元の画面でもApp barっぽいのがあったのに、activity\_main.xmlに新たに`<com.google.android.material.appbar.AppBarLayout>`が追加されたことが不思議ではありませんでしたか？　こんなことをした理由は、元の画面でのApp barっぽいのは制御ができない（少なくとも私は制御のやり方をしらない）から。だから、新しいのを追加したわけですな。

新しいのを追加した以上は古いのを削除しなければならないわけで、そのためにAndroidManifest.xmlを修正します。

~~~ xml
<?xml version="1.0" encoding="utf-8"?>
<manifest
    xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.tail_island.jetbus">

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">

        <activity android:name=".MainActivity" android:theme="@style/AppTheme.NoActionBar">  <!-- android:theme属性を追加 -->
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
~~~

`AppTheme.NoActionBar`というスタイルにするように指定しているわけ……なのですけど、この`AppTheme.NoActionBar`は自分で作らなければならないんですよ。無駄に感じる作業が続いてかなり腹が立ってきた頃かと思いますが（私は、毎回この作業の途中で独り言で文句を言っているらしくて、周囲に不気味がられています）、アプリ開発につき1回だけ、しかもコピー＆ペーストで済む作業ですので頑張りましょう。[Project]ビューのres/values/styles.xmlを、以下に変更します。

~~~ xml
<?xml version="1.0" encoding="utf-8"?>
<resources>

    <style name="BaseAppTheme" parent="Theme.MaterialComponents.Light.DarkActionBar">
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
    </style>

    <style name="AppTheme" parent="BaseAppTheme" />

    <style name="AppTheme.NoActionBar">
        <item name="windowActionBar">false</item>
        <item name="windowNoTitle">true</item>
    </style>

    <style name="AppTheme.AppBarOverlay" parent="ThemeOverlay.MaterialComponents.Dark.ActionBar" />

    <style name="AppTheme.PopupOverlay" parent="ThemeOverlay.MaterialComponents.Light" />

</resources>
~~~

App barとNavigation drawerとライブラリのNavigationを組み合わせましょう。MainActivity.ktを開いて、以下に変更してください。

~~~ kotlin
package com.tail_island.jetbus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.tail_island.jetbus.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Bindingを生成します
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).also {
            // NavigationControllerを初期化します
            findNavController(R.id.navHostFragment).apply {
                // レイアウトで設定したToolbarとDrawerLayoutと協調させます。また、BookmarksFragmentをルートにします。itは、ActivityMainBindingのインスタンスです
                NavigationUI.setupWithNavController(it.toolbar, this, AppBarConfiguration(setOf(R.id.bookmarksFragment), it.drawerLayout))

                // SplashFragmentでは、ツールバーを非表示にします
                addOnDestinationChangedListener { _, destination, _ ->
                    it.appBarLayout.visibility = if (destination.id == R.id.splashFragment) View.GONE else View.VISIBLE
                }
            }
        }
    }

    // 不整合の辻褄をあわせます。なんで我々がと思うけど我慢……。
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {  // Navigation drawerが開いているときは、[戻る]ボタンでクローズします
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return
        }

        if (findNavController(R.id.navHostFragment).currentDestination?.id == R.id.bookmarksFragment) {  // AppBarConfiguration()したのでツールバーはハンバーガー・アイコンになっていますが、それでもバック・ボタンでは戻れちゃうので、チェックします
            finish()
            return
        }

        super.onBackPressed()
    }
}
~~~

このコードは少し複雑ですから、解説を。

NavigationとNavigation drwerとApp barはとても良くできていて、上のコードのように`NavigationUI.setupWithNavController()`で結合できるのですけど、少しだけ、AndroidのAPIとの不整合があります。不整合その1は、Navigation drawerが開いているときに[戻る]ボタンが押されたときの動作です。Navigation drawerが表示されるというのは`<com.google.android.material.navigation.NavigationView>`に画面遷移したように見えるのですけど、内部的には画面遷移になっていないので、[戻る]ボタンを押してもNavigation drawerは閉じられません。これではユーザーが混乱しますから、`onBackPressed()`をオーバーライドして「Navigation drawerが開いている場合は閉じる」処理を追加しました。不整合その2は、本アプリでは、NavigationのXMLでのトップ／レベルの画面は`SplashFragment`なのですけど、アプリ的には`BookmarksFragment`がトップ・レベルであることです。なので、上のコードの`AppBarConfiguration(setOf(R.id.bookmarksFragment), ...)`でトップ・レベルの指定をしているのですけど、やっぱり[戻る]ボタンでの動作がおかしくなっちゃう。`BookmarksFragment`ではApp barの左がハンバーガー・アイコンになって戻れない様になっていて正しいのですけど、[戻る]ボタンを押すと`SplashFragment`に戻ってしまいます。なのでやっぱり、`onBackPressed()`の中に遷移を制御する処理を追加しました。

で、`onBackPressed()`はメソッドですから、`onCreate()`と`Binding`を共有したい場合は属性を追加しなければなりません。だから`binding`という属性を追加しなければならないのですけど、この`binding`は、`onCreate()`が呼ばれるまで値を設定できないという問題があります。しょうがないので最初は`null`を設定する……のは、KotlinのようなNull安全を目指している言語では悪手です。今回のように、`binding`の値が設定される前に使用されないことを保証できる（`onBackPressed()`は`onCreate()`が終わった後にしか呼び出されないので、保証できます）場合は、プログラマーの責任で属性を`lateinit`として定義できます。`lateinit`にすると初期値を設定しなくてよいので、ほら、`binding`の型を`null`を許容「しない」`ActivityMainBinding`に設定できました。

あと、上のコードでやっているのは、`SplashFragment`でApp barを消しています。何もしないとApp barに[戻る]アイコンが表示されてしまいますし、調べた限りでは、他のアプリでもスプラッシュ画面にはApp barがありませんでしたから。

ともあれ、これで作業完了のはず。試してみましょう

動画

うん、完璧ですな。Navigationよ今夜もありがとう。Navigationくらいに楽チンな、App barとNavigation drawerをどうにかしてくれるライブラリがJetpackに追加されないかなぁ……。

# [Retrofit2](https://github.com/tail-island/jetbus/tree/retrofit2)

さて、本アプリは[公共交通オープンデータセンター](https://www.odpt.org/)が提供してくれるデータが無いと動きようがありませんので、個々の画面の機能を作っていく前に、HTTP通信でWebサーバーからデータを取得する処理を作ってみましょう。残念なことにJetpackにはHTTP通信の機能がありませんので、外部のライブラリであるRetrofit2を使用します。

## Retrofit2の組み込み

Retrofit2を組み込むために、build.gradelを変更します。

~~~ gradle
dependencies {
    ...

    implementation 'androidx.room:room-ktx:2.2.0-beta01'
    implementation 'com.squareup.retrofit2:retrofit:2.6.1'  // 追加
    implementation 'com.squareup.retrofit2:converter-gson:2.6.1'  // 追加
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"

    ...
}
~~~

これで、いつでもHTTP通信できるわけですけど、その前に、公共交通オープンデータセンターにユーザー登録しないとね。

## 公共交通オープンデータセンターへのユーザー登録

[公共交通オープンデータセンター](https://odpt.org/)のサイトを開いて、下の方にある[[公共交通オープンデータセンター開発者サイト](https://developer.odpt.org/ja/info)]リンクをクリックします。で、真ん中あたりにある[ユーザ登録のお願い]ボタンを押して、もろもろ入力して[この内容で登録を申請する]ボタンを押すと、「最大2営業日」で登録完了のメールが送られてきます。……どうして、最大とはいえ2営業日もかかるんだろ？

登録が完了したらログインして、右上の[Account]メニューの[アクセストークンの確認・追加]を選ぶと、アクセス・トークンが表示されます。まずはこれをコピーしてください。この情報の保存先としては、リソースを使用します。プロジェクトを右クリックして、[New] - [Android Resource File]メニューを選択して、[File name]に「odpt」を入力して[Resource type]を「Value」に設定し、作成されたodpt.xmlに以下を入力します。

~~~ xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="consumerKey">アクセス・トークンをここにペースト</string>
</resources>
~~~

よし、これで公共交通オープンデータセンターのデータを使い放題……なのですけど、いったい、どんなデータがあるのでしょうか？

## 公共交通オープンデータセンターのデータ

[公共交通オープンデータセンター開発者サイト](https://developer.odpt.org/)の[[API仕様](https://developer.odpt.org/documents)]リンクをクリックすれば、公共交通オープンデータセンターが提供するデータの仕様が分かります。

左に表示されている目次の[4. ODPT Bus API]リンクをクリックすると、バスに関するどのようなデータをとれるのかが分かります。見てみると、以下の6つの情報を取得できるみたい。

* `odpt:BusstopPole`（バス停（標柱）の情報）
* `odpt:BusroutePattern`（バス路線の系統情報）
* `odpt:BusstopPoleTimetable`（バス停(標柱)の時刻表）
* `odpt:BusTimetable`（バスの便の時刻表）
* `odpt:BusroutePatternFare`（運賃情報）
* `odpt:Bus`（バスの運行情報）

バスの運行情報がありますから、バスの車両の接近情報は表示できそうですね。なんかいろいろ細かいことが書いてありますけど、習うより慣れろってことで、Webブラウザを開いて、まずはバス停を取得してみましょう。「https://api.odpt.org/api/v4/odpt:BusstopPole?acl:consumerKey=ACL\_CONSUMERKEY&odpt:operator=odpt.Operator:Toei」（ACL\_CONSUMERKEYの部分には、ユーザー登録で取得したアクセストークンをコピー＆ペーストしてください）を開いてみます。なるほどバス停のデータが取得できている……のですけど、検索しても、こんなダラダラ生きている私を雇用してくださってるとてもありがたい「日本ユニシス本社前」という、私が会社からの帰りに使うバス停が見つかりませんでした。

図

データが欠落しているのは、API仕様の[1.3. インターフェース]の中の[1.3.1. 留意点]に「APIによって出力される結果がシステムの上限件数を超える場合、上限件数以下にフィルターされた結果が返る」とあって、リンクを辿って調べてみたら、その上限件数は1,000件だったためです（2020年3月現在）。`odpt:BusstopPole`はバス停ではなくてバス停の標柱（方面ごとに立っているアレ）で大量なので、余裕で1,000件を超えてしまって切り捨てられちゃったみたい。

というわけで、データ検索APIではなくて、データダンプAPIを使いましょう。「https://api.odpt.org/api/v4/odpt:BusstopPole.json?acl:consumerKey=ACL\_CONSUMERKEY」（URIに「.json」が追加されました。あと、先ほどと同様に、ACL\_CONSUMERKEYの部分には、ユーザー登録で取得したアクセストークンをコピー＆ペーストしてください）を開いて、データの取得が完了するまで、しばし待ちます。日本全国津々浦々のバス停の標柱全てという大量のデータなので、時間がかかるんですよ……。はい、今度は、「日本ユニシス本社前」のデータが見つかりました。

図

ドキュメントによれば、`odpt:BusstopPole`と`odpt:BusroutePattern`は`owl:sameAs`属性で互いに紐付けられるので、出発バス停の標柱群（一つのバス停に複数の標柱がある）と到着バス停の標柱群を指定すれば、その両方に紐付いている`odpt:BusroutePattern`を抽出することで路線を見つけることができそう。ただ、`odpt:BusroutePattern`の検索APIのクエリー・パラメーターには`odpt:BusstopPole`がなかったので、抽出は自前のコードでやらなければなりませんけどね。どうせ抽出を自前のコードでやるのであれば、`odpt:BusroutePattern`もデータダンプAPIでまるっと取得してしまうことにしましょう。

次。`odpt:Bus`です。APIの[4.ODPT Bus API]の中の[4.2.パス]を読むと、`odpt:BusroutePattern`をクエリー・パラメーターにとることができて、[1.4. データ検索API (/v4/RDF_TYPE?)]の中の[1.4.1. フィルター処理]によれば、カンマ区切りにすればOR条件での検索になるらしい。これなら、複数の路線のバスの運行情報を一発で取得できる……のですけど、[4.3. 定義]の中の[4.3.1. odpt:Bus]の記述によれば、どのバス停を通過したのかは分かるけど、あとどれくらいで今私の目の前にあるバス停に到着するのかは分からないみたい。なので、`odpt:BusTimetable`も取得して、時刻表からバス停とバス停の間の時間を調べて、それを足し合わせて到着までの予想時間としましょう。だから、`odpt:BusTimetable`を取得する処理も作らないとね。

あと、API仕様にはバスの現在の位置によって`odpt:fromBusstopPole`属性（直近に通過した、あるいは停車中のバス停）と`odpt:BusstopPole`（次に到着するバス停）の情報がいろいろ変わると書いてあるのですけど、都営バスの実際のデータを取得して調べてみると、どうも現在位置がどうであれ`odpt:fromBusstopPole`属性と`odpt:toBusstopPole`属性の両方が設定されているみたい。ならば手を抜いて`odpt:fromBusstopPole`属性だけを見ればいいかなぁと。さらに、`odpt:BusTimetable`の`odpt:calendar`属性（平日時刻表とか休日時刻表とか）は[2.3. 定義]の中の[2.3.1. odpt:Calendar]に書いてある汎用データを使用していませんでした。なんだか、都営バス独自の特殊なデータが並んでいやがります……。まぁ、今回の`odbt:BusTimetable`の使用目的は到着時間を計算するための元ネタでしかありませんから、`odpt:calendar`も無視することにしましょう。プログラムが簡単になるしね。

そうそう、`odpt:BusstopPole`と`odpt:BusroutePattern`、`odpt:BusTimetable`は、データの取得に時間がかかる上にほとんど変更がない情報ですから、RDBMSにキャッシュすることにしましょう。RDBMSを使えば、突き合わせの処理が楽になりますしね。

## Webサービスの定義

やることが決まりましたので、Webサービスの定義を作りましょう。でもその前に、データ受け渡しのためのクラスを作成します。まずは、`odpt:Bus`を表現するクラスを作成します。モデルを入れるための`model`パッケージを作成して、その中にBus.ktファイルを作成して、以下のコードを入力してください。

~~~ kotlin:Bus.kt
package com.tail_island.jetbus.model

import com.google.gson.annotations.SerializedName

data class Bus(
    @SerializedName("owl:sameAs")
    var id: String,

    @SerializedName("odpt:busroutePattern")
    var routeId: String,

    @SerializedName("odpt:fromBusstopPole")
    var fromBusStopPoleId: String
)
~~~

なんでクラスの後ろの括弧が波括弧（`{}`）じゃなくて丸括弧（`()`）なんだという疑問を抱いたJavaプログラマの方がいるかもしれませんので、ここで少しだけKotlinの解説をさせてください。

Kotlinでは、Scalaと同様に、クラス定義の際にコンストラクタの引数を定義できます。`class Foo(param1: Type, param2: Type)`みたいな感じ。そんなところに引数を書いたらコンストラクタでの処理はどこに書くんだよと思うかもしれませんけど、コンストラクタの処理は普通は書きません（どうしても書きたい場合には`init {}`という構文があるのでご安心を）。どうして普通は書かないのかと言うと、コンストラクタでの処理は一般にインスタンスの状態の設定で、インスタンスの状態である属性の定義ではコンストラクタの引数が使えるから。`class Foo(param: Type) { var bar = param.doSomething() }`みたいな感じです。

あと、Kotlinはミュータブル（可変）の変数は`var`、イミュータブル（不変）の変数は`val`で宣言するのですけど、`var`や`val`は先のコードのようにプロパティの定義でも使えます（`var`だと`get`と`set`が生成されて、`val`だと`get`だけが生成される）。これがコンストラクタの引数にも適用されて、`class Foo(var param1: Type, val param2: Type) {}`と書けば、ミュータブルなプロパティの`param1`とイミュータブルなプロパティの`param2`が生成されるというわけ。

さらに、データを保持するためのクラス作成専用の`data class`という構文があります。`data class`を使うと、`equals()`メソッドや`hashCode()`メソッド、`toString()`メソッド、`copy()`メソッド（あと`componentN()`メソッド）が自動で生成されてとても便利。さらに、`data class`でメソッドの定義が不要な場合は`{}`を省略できるので、上のようなコードになるわけですな。

さて、Retrofit2（が内部で使用しているGSON）的に重要なのは、`@SerializedName`の部分です。これはアノテーションと呼ばれるもので、ライブラリやコード・ジェネレーターが参照します。Retrofit2（が内部で使用しているGSON）は、`@SerializedName`アノテーションを見つけると、JSONを作成する際に`@SerializedName`アノテーションの引数で指定した文字列を使用してくれます。これで、`owl:sameAs`のようなKotlinでは許されない名前の属性を持ったJSONでも取り扱えるようになるというわけ。ふう、これで`odpt:Bus`を受け取る準備は完璧です。

同様に残りの`odpt:BusstopPole`と`odpt:BusroutePattern`、`odpt:BusTimetable`も……と考えたのですけど、これらはRDBMSにキャッシュすることにしましたから、RDBMSのレコードを表現するクラスとごっちゃになってしまって混乱しそう。だから今回はクラスを作成しないで、Retrofit2（が内部で使用しているGSON）が提供する`JsonArray`（検索APIは配列を返すので、`JsonObject`ではなくて`JsonArray`にしました）を使用します。

以上でWebサービスを呼び出した結果を受け取るクラスの型が全て決まりましたので、Webサービスを呼び出す部分をRetrofit2を使用して作成しましょう。といっても、実装は簡単で`interface`を定義するだけ。HTTP通信のメソッドをアノテーション（今回は`@GET`）で設定して、Webサービスの引数を同様にアノテーション（今回はクエリー・パラメーターなので`@Query`）で定義するだけですが。モデルを作成した`model`パッケージの中にWebService.ktファイルを作成して、以下を入力してください。

~~~ kotlin:WebService.kt
package com.tail_island.jetbus.model

import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WebService {
    @GET("/api/v4/odpt:BusstopPole.json")
    fun busstopPole(@Query("acl:consumerKey") consumerKey: String): Call<JsonArray>

    @GET("/api/v4/odpt:BusroutePattern.json")
    fun busroutePattern(@Query("acl:consumerKey") consumerKey: String): Call<JsonArray>

    @GET("/api/v4/odpt:BusTimetable")
    fun busTimeTable(@Query("acl:consumerKey") consumerKey: String, @Query("odpt:busroutePattern") routePattern: String): Call<JsonArray>

    @GET("/api/v4/odpt:Bus")
    fun bus(@Query("acl:consumerKey") consumerKey: String, @Query("odpt:busroutePattern") routePattern: String): Call<List<Bus>>
}
~~~

はい、完成です。楽チン。

## Webサービスの呼び出し

とは言っても`interface`は呼び出しができませんので、なんとかして（Retrofit2のAPIが要求する形で）インスタンスを生成する方法を調べなければなりません。あとですね、Webサービスの呼び出しには時間がかかることにも、考慮が必要です。というのも、AndroidではUIの制御はメイン・スレッド*のみ*から実施できることになっていて、だからメイン・スレッドでWebサービスのような時間がかかる処理をすると、その処理の間は画面が無反応になっちゃう。だから、Webサービスの呼び出しは*別スレッドでやらなければならない*んです（そうしないと、画面が無反応になる以前に実行時エラーとなります）。

と、こんな感じでいろいろ複雑なので、とりあえずコードを書いてみましょう。まずは、`odpt:BusstopPole`を取得してみます。最初に表示される画面である`SplashFragment`の、画面に表示される直前に呼び出される`onStart()`メソッドを追加して、以下の処理を書き加えます。

~~~ kotlin
package com.tail_island.jetbus

// ....

import android.util.Log
import com.tail_island.jetbus.model.WebService
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import kotlin.concurrent.thread

class SplashFragment: Fragment() {
    // ...

    override fun onStart() {
        super.onStart()

        // リソースからアクセス・トークンを取得します。consumerKeyって名前は提供側の用語なので嫌だけど、公共交通オープンデータセンターがこの名前を使っちゃっているからなぁ……
        val consumerKey = getString(R.string.consumerKey)

        // WebServiceのインスタンスを生成します。スコープ関数が存在しない哀れな環境向けのFluentなBuilderパターン……
        val webService = Retrofit.Builder().apply {
            baseUrl("https://api.odpt.org")
            addConverterFactory(GsonConverterFactory.create())
        }.build().create(WebService::class.java)

        // Webサービスを呼んでいる間は画面が無反応になるのでは困るので、スレッドを生成します。今は素のスレッドを使用していますけど、後でもっとかっこいい方式をご紹介しますのでご安心ください
        thread {
            try {
                // WebServiceを呼び出します
                val response = webService.busstopPole(consumerKey).execute()

                // HTTP通信した結果が失敗の場合は、エラーをログに出力してnullを返します
                if (!response.isSuccessful) {
                    Log.e("SplashFragment", "HTTP Error: ${response.code()}")
                    return@thread
                }

                // レスポンスのボディは、interfaceの定義に従ってJsonArrayになります
                val busStopPoleJsonArray = response.body()

                // nullチェック
                if (busStopPoleJsonArray == null) {
                    return@thread
                }

                // JsonObjectに変換して、都営バスのデータだけにフィルターして、最初の10件だけで、ループします
                for (busStopPoleJsonObject in busStopPoleJsonArray.map { it.asJsonObject }.filter { it.get("odpt:operator").asString == "odpt.Operator:Toei" }.take(10)) {
                    // 確認のために、いくつかの属性をログ出力します
                    Log.d("SplashFragment", "${busStopPoleJsonObject.get("owl:sameAs")}")
                    Log.d("SplashFragment", "${busStopPoleJsonObject.get("dc:title")}")
                    Log.d("SplashFragment", "${busStopPoleJsonObject.get("odpt:kana")}")
                }

            } catch (e: IOException) {
                // HTTP以前のエラーへの考慮も必要です。ログ出力しておきます
                Log.e("SplashFragment", "${e.message}")
            }
        }
    }
}
~~~

「公共交通オープンデータセンターへのユーザー登録」で設定した文字列リソースは、`getString()`メソッドで取得できます。`Retrofit`の生成APIはFluentなBuilderパターンで作られているんですけど、Fluentが主でBuilderパターンの意味は少なくて、`apply`スコープ関数があるKotlinでは無意味……。でもしょうがないので、折衷案なスタイルのコードとなりました。これで初期化作業は終了。

前述したようにWebサービスの呼び出しは別スレッドでやらなければならないのですけど、Kotlinなら`kotlin.concurrent.thread`でラムダ式を別スレッドで実行できて便利です。でもまぁ、コード中のコメントにも書きましたけど、後の章で述べるコルーチンを使えばさらにかっこよく書けるので、この書き方はすぐに忘れちゃって大丈夫なんだけどね。

別スレッドの中で、先程取得した`WebService`のインスタンスの`busstopPole()`メソッドを呼び出して`Call`インスタンスを取得して、さらに`execute()`しています。こんな面倒な形になっているのは、同期で呼び出す場合にも非同期（コールバック方式）で呼び出す場合にも対応しているから。で、今回は可読性が高い同期の`execute()`メソッドを使用しました。あとは、`isSuccessful`でHTTPのエラーが発生していないことを確認して、`body()`を取得して、これで通信は終了。

ここまででWebサービスから取得した`JsonArray`は、テストのために、`Log.d()`で内容をログ出力して終了です。あとは、HTTP以前のエラー（たとえばサーバーが見つからない等）に対応するために、`try/catch`します。

と、こんな感じでRetrofit2は使用できるのですけど、上のコードは、実はかなり格好悪いコードなんですよ。Kotlinの良さを全く引き出せていません。なので、修正しましょう。

## Null安全の便利機能を使ってみる

Kotlinでは、`NullPointerException`が発生するようなコードは、基本的にコンパイルできません。たとえば、先程のコードの「nullチェック」とコメントした`if`をコメント・アウトすると、コンパイル・エラーとなります。

Kotlinがこのような離れ業をするには変数や関数の戻り値が`null`になりえるかどうかをコード上で表現できなければならないわけで、Kotlinは、それを型名の後ろに`?`がつくかどうかで表現しています。`Int`なら`null`になることはない、`Int?`ならば`null`になる可能性があるって感じ。上のコードのRetrofit2の`Response`の`body()`は`JsonArray?`を返すので、その戻り値のメソッドを呼ぶとコンパイル・エラーになるというわけ。

で、Kotlinで`Foo?`の変数を`Foo`にするのは簡単で、`if`等で`null`でないことを確認すればよい。先程のコードの`if`がまさにそれなわけですな。でも、そんな`if`だらけのコードを書くのは大変すぎるし読みづらすぎるので、いくつかの便利な記法があります。

１つ目は、`!!`。文法的には`null`の可能性があるように見えるかもしれないけれど、`null`でないことを我々プログラマーが保証してやるぜって場合です。`var x: Foo? = null; x.bar()`はコンパイル・エラーになりますけど、`var x: Foo? = null; x!!.bar()`ならコンパイルは通ります。もちろん、実行時に`NullPointerException`が出るでしょうけど。

2つ目は、`?.`。`null`ならばメソッドを呼び出さないで`null`を返して、そうでなければメソッドを実行してその戻り値を返すという記法です。`var x: Foo? = null; var y = x?.bar()`は、コンパイルも通りますし`NullPointerException`も出ません。`bar()`は実行されず、`y`には`null`がセットされます。

3つ目は、`?:`。`?.`の反対で、`null`の場合に実行させたい処理を記述できます。`var x: Foo ?= null; var y = x?.bar() ?: "BAR"`なら、`y`の値は`null`ではなく「BAR」になります。

これらの記法ともはや見慣れたスコープ関数を組み合わせれば、先程のコードはもっときれいになります。そう、こんな感じ。

~~~ kotlin
run {
    val response = webService.busstopPole(consumerKey).execute()

    if (!response.isSuccessful) {
        Log.e("SplashFragment", "HTTP Error: ${response.code()}")
        return@run null
    }

    response.body()  // ラムダ式では、最後の式の結果がラムダ式の戻り値になります

}?.let { busStopPoleJsonArray ->  // ?.なので、run { ... }の結果がnullならlet { ... }は実行されません。
   for (busStopPoleJsonObject in busStopPoleJsonArray.map { it.asJsonObject }.filter { it.get("odpt:operator").asString == "odpt.Operator:Toei" }.take(10)) {
        Log.d("SplashFragment", "${busStopPoleJsonObject.get("owl:sameAs")}")
        Log.d("SplashFragment", "${busStopPoleJsonObject.get("dc:title")}")
        Log.d("SplashFragment", "${busStopPoleJsonObject.get("odpt:kana")}")
    }
} ?: return@thread  // run { ... }の結果がnullならリターン
~~~

うん、マシになりました。でもまだ駄目です。他のWebサービスも呼び出す場合は、`run { ... }`の中のほとんどをもう一回書かなければならないでしょうから。

## 高階関数を作ってみる

というわけで、関数化しましょう。こんな感じ。

~~~ kotlin
private fun <T> getWebServiceResultBody(callWebService: () -> Call<T>): T? {
    val response = callWebService().execute()

    if (!response.isSuccessful) {
        Log.e("SplashFragment", "HTTP Error: ${response.code()}")
        return null
    }

    return response.body()
}
~~~

引数は関数です。このような関数を引数にする関数を高階関数と呼びます。Kotlinでは、`(引数) -> 戻り値`で関数そのものの型を表現できて、上のコードの`<T>`の部分はテンプレートです。呼び出し側はこんな感じ。

~~~ kotlin
getWebServiceResultBody { webService.busstopPole(consumerKey) }?.let { busStopPoleJsonArray ->
    for (busStopPoleJsonObject in busStopPoleJsonArray.map { it.asJsonObject }.filter { it.get("odpt:operator").asString == "odpt.Operator:Toei" }.take(10)) {
        Log.d("SplashFragment", "${busStopPoleJsonObject.get("owl:sameAs")}")
        Log.d("SplashFragment", "${busStopPoleJsonObject.get("dc:title")}")
        Log.d("SplashFragment", "${busStopPoleJsonObject.get("odpt:kana")}")
    }
}
~~~

もはや見慣れたコードですな。前にも述べましたけど、関数はラムダ式で定義することができて、最後のパラメーターがラムダ式の場合はそのパラメーターは括弧の外にだす慣習があって、そして、ラムダ式だけを引数にする場合は括弧を省略できるので、このようなすっきりした記述になります。

ついでですから、`odpt:BusroutePattern`を取得して、その路線の`odpt:Bus`を取得するコードも書いてみましょう。

~~~~ kotlin
getWebServiceResultBody { webService.busroutePattern(consumerKey) }?.let { busroutePatternJsonArray ->
    for (busroutePatternJsonObject in busroutePatternJsonArray.map { it.asJsonObject }.filter { it.get("odpt:operator").asString == "odpt.Operator:Toei" }.take(10)) {
        Log.d("SplashFragment", "${busroutePatternJsonObject.get("owl:sameAs")}")
        Log.d("SplashFragment", "${busroutePatternJsonObject.get("dc:title")}")

        for (busstopPoleOrderJsonObject in busroutePatternJsonObject.get("odpt:busstopPoleOrder").asJsonArray.take(10).map { it.asJsonObject }) {
            Log.d("SplashFragment", "${busstopPoleOrderJsonObject.get("odpt:index")}")
            Log.d("SplashFragment", "${busstopPoleOrderJsonObject.get("odpt:busstopPole")}")
        }

        getWebServiceResultBody { webService.bus(consumerKey, busroutePatternJsonObject.get("owl:sameAs").asString) }?.let { buses ->
            for (bus in buses.take(10)) {
                Log.d("SplashFragment", bus.id)
                Log.d("SplashFragment", bus.routeId)
                Log.d("SplashFragment", bus.fromBusStopPoleId)
            }
        }
    }
}
~~~~

`getWebServiceResultBody()`メソッドを定義済みなのでとても簡単です。`odpt:Bus`の方は、クラスを定義したのでさらに簡単なコードになっていますな。

## アプリへの権限の付与

というわけで、コーディングは終わり。早速実行……するまえに、アプリにインターネット・アクセスの権限を付加しなければなりません。[Project]ビューの[app] - [manifests]の下のAndroidManifest.xmlを開いて、`<uses-permission>`タグを追加してください。

~~~ xml
<?xml version="1.0" encoding="utf-8"?>
<manifest
    xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.tail_island.jetbus">

    <uses-permission android:name="android.permission.INTERNET" />  <!-- 追加 -->

    <application
        ....>
        ....
    </application>

</manifest>
~~~

これで本当に全て完了。実行してみましょう。`Log`で出力した結果は、Android Studioの[Logcat]ビューで見ることができます。

図

うん、正しくデータを取れていますね。Retrofit2ならWebサービス呼び出しはとても楽チン。

# [Room](https://github.com/tail-island/jetbus/tree/room)

前章で取得した公共交通オープンデータセンターのデータは、取得に時間がかかる上に変更が少ないので、RDBMSにキャッシュしたい。というわけで、Android OS内蔵のSQLite3への抽象化レイヤを提供するRoomを使用してみます。そこそこ便利ですよ。

## Roomが、他のO/Rマッピング・ツールとは異なるところ

さて、O/Rマッピングというと、データを表現するクラスを元にテーブルが生成され（もしくは、テーブル定義からクラスが生成され）、黙っていてもINSERTやUPDATE、DELETEをするメソッドが自動生成され、さらに、条件を指定するSELECTがメソッド呼び出しで実施できるようになって、ついにはテーブルとテーブルの間の関連を辿るメソッドが魔法のように現れる重厚長大なツールを想像するかもしれません。

でも、ごめんなさい、Roomは違うんです。それも、悪い意味で。

まず、テーブルとテーブルの間の関連を辿ることはできません。Roomでは、テーブル単位での操作しかできないんです。もちろん理由はあって、データベースの操作という重い処理を、UI制御を担当するメイン・スレッドでやるわけにはいかないためです。メイン・スレッドの中で関連を辿るプロパティを読んだらデータベース・アクセスが動いてUIが止まってしまう……なんてのは困りますもんね。

あと、条件を指定してのSELECTでは、条件を指定する部分はSQL文を文字列としてベタ打ちしなければなりません。呼び出す側からはオブジェクト指向っぽく見えるけど、実装する側からはゴリゴリSQLなんです。まぁ、SQL文を書くのは面倒に感じますけど、`find(column: String, value: Int)`みたいなボロっちいメソッドしか提供されなくて効率が悪いSQLが実行されちゃうよりはマシです。後で説明しますけど、テーブルとテーブルの間の関連を辿る機能がない問題も、SQL文をベタ書きできるならあまり問題にはなりませんし。

一般的なO/Rマッピング・ツールの残りの機能は提供されるのですけど、残っているのは、アノテーションをつけることでINSERTやUPDATE、DELETEが自動生成されるのと、データを表現するクラスを書けばテーブルが生成されることだけです。ぶっちゃけ、Roomが提供する機能はこれだけなんですよ。

と、ここまでを読んで「そんなヘッポコなのはO/Rマッピング・ツールじゃない」と思われたかもしれませんけど、O/Rマッピングとはオブジェクトとリレーションのマッピングで、この「リレーション」というのはリレーショナル・データベース用語ではテーブルのことなんです（テーブルとテーブルの間の関連は、リレーション*シップ*）。だから、RoomをO/Rマッピングと呼んでも、言葉の意味では問題ないかなぁ。

まぁ、いろいろとひどいことを書きましたけど、私はRoomが好きです。「こういうのでいいんだよ。こういうので」ってヤツですな。他のO/Rマッピング・ツールがオーバースペックなだけ。ほら、前の章でJSONそのものを表現するJsonObjectを使った場合（`obpt:BusstopPole`や`odpt:BusroutePattern`の場合）より、JSONのデータをクラスにマッピングさせた場合（`odpt:Bus`の場合）のほうがコードが簡単だったでしょ？　あれと同じで、テーブルのレコードをインスタンスにマップしてくれるだけでも、プログラミングはとても楽になるんです。単純な機能しかないので使うの簡単ですし、SQLベタ書きなのでリレーショナル・データベースの機能を引き出しやすいですしね。

## データ構造の設計

と、他のO/Rマッピング・ツール経験者への言い訳が終わったところで、作業に入りましょう。まずは、データ構造の設計です。私は設計文書を書かないでいきなりコードを書き出すタイプの人間ですけど、そんな私でもプログラミング前にデータ構造だけは設計します。ER図かUMLのクラス図を描くだけですけどね。こんなの。

図

今回はER図にしてみました。手書きのなぐり書きですけど、設計はこれで十分。だって、これでアプリが機能を提供できるかを確認できますから。試しにやってみましょう。到着バス停を指定する画面を作れるか、確認してみます。

出発バス停を指定する画面ではしょうがないのですべてのバス停を表示しますけど、今確認している到着バス停を選ぶ画面では、出発バス停とつながっている（乗り換え無しで行ける）バス停だけを表示して欲しいですよね？　この機能を実現できるかを確認します。

`BusStop`から`BusStopPole`を辿れることは、図から明らかです。`BusStopPole`まで行ければ、`RouteBusStopPole`を経由して`Route`を取得できます。`Route`が分かれば、今度は逆に辿ることで`BusStop`まで行けます。ほら、出発バス停が指定されれば、そこから、出発バス停とつながっているバス停の一覧を取得できます。これで、到着バス停を指定する画面を余裕で作れることが確認できました。

あと、出発バス停と到着バス停が分かれば、対象となる`Route`が上りなのか下りなのかの判断ができます。`RouteBusStopPole`には`order`があって、これは`Route`における順序です。だから、出発の`BusStop`に関連付けられた`RouteBusStopPole.order`が到着の`BusStop`に関連付けられた`RouteBusStopPole.order`よりも小さい方の`Route`を選べば、上りか下りのどちらかとなるというわけ。SQLで書くと、こんな感じです。

~~~ sql
SELECT Route.*
FROM BusStopPole AS ArrivalBusStopPole
INNER JOIN RouteBusStopPole AS ArrivalRouteBusStopPole ON
           ArrivalRouteBusStopPole.busStopPoleId = ArrivalBusStopPole.id
INNER JOIN Route ON
           Route.id = ArrivalRouteBusStopPole.routeId
INNER JOIN RouteBusStopPole As DepartureRouteBusStopPole ON
           DepartureRouteBusStopPole.routeId = Route.id
INNER JOIN BusStopPole AS DepartureBusStopPole ON
           DepartureBusStopPole.id = DepartureRouteBusStopPole.busStopPoleId
WHERE ArrivalRouteBusStopPole.'order' > DepartureRouteBusStopPole.'order' AND
      DepartureBusStopPole.busStopName = "日本ユニシス本社前" AND
      ArrivalBusStopPole.busStopName = "塩浜二丁目"
~~~

前の方でRoomにはテーブル間の関係を辿る機能がないと書きましたけど、このSQLのように`JOIN`で繋げば（もしくは、後の章で述べるように副問合せを使えば）他のテーブルのカラムを検索条件に指定することは可能です。だから、Roomにテーブルの間の関連を辿る機能がなくても大丈夫なんです。そもそも、この処理なんて、普通のO/Rマッピング・ツールで書くとかえって大変ですよ。出発の`BusStopPole`の集合を作成して`RouteBusStopPole`を経由して`Route`の集合を取得し、到着でも同じ処理をして、それぞれの`Route`の集合を`union`して、さらにその後に`order`を使用してフィルタリングするような処理になっちゃうでしょうからね。

さて、`Route`が分かれば`TimeTable`を取得できます。ただ、バスは一つの路線を日に何回も行き来していますから、`Route`と`TimeTable`の関係は1対多となっています。よって、複数ある`TimeTable`の中から一つを選ばなければなりません。最初の一つとかランダムで一つとかを選んでもいいのですけど、道路が空いているときと混んでいるときで時刻表は変わるだろうと考えて、到着バス停に関連付けられた`TimeTableDetail.arrival`が現在時刻に最も近いものを選ぶことにしましょう。

あとは、データベースでは管理しないけどER図には追加しておいた`Bus`に関連付けられた`BusStopPole`に関連付けられた`TimeTableDetails.arrival`と、出発`BusStopPole`と関連付けられている`TimeTableDetails.arrival`の差を計算すれば、ほら、これで到着までの時間になります。バスの接近情報を表示する画面も完璧ですな。

と、こんな感じでシミュレーションできたので、これで設計は終わり！　プログラミングに入りましょう。

## エンティティの定義

設計したER図のエンティティをプログラミングします。とは言っても、Retrofit2のところでやったデータ受け渡しのクラスの作成と似た作業、`data class`を作るだけの簡単作業です。まずは、`BusStop`を作ってみましょう。

~~~ kotlin
package com.tail_island.jetbus.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BusStop(
    @PrimaryKey
    var name: String,

    var phoneticName: String?
)
~~~

RoomのエンティティであることをRoomに伝えるために、`@Entity`アノテーションを追加しておきます。あと、リレーショナル・データベースではレコードを識別するための主キーが必要なので、`@PrimaryKey`アノテーションで主キーを指定しています。

`BusStop`に関連付けられる`BusStopPole`も作りましょう。

~~~ kotlin
package com.tail_island.jetbus.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = BusStop::class, parentColumns = ["name"], childColumns = ["busStopName"])])
data class BusStopPole(
    @PrimaryKey
    var id: String,

    @ColumnInfo(index = true)
    var busStopName: String
)
~~~

`BusStopPole`では、`BusStop`と関連付けられていることを示すために`@Entity`アノテーションの`foreignKeys`を指定してます。参照整合性と呼ばれるアレですな。あと、`busStopName`で検索するときに速度が出るように`@ColumnInfo`アノテーションの`index`に`true`を指定しています。データベース管理者が検索のパフォーマンス・チューニングのときに貼るインデックスのことですな。

参照整合性というのは、テーブルのレコード間の関連を正しく保つ仕組みです。存在しない`BusStop`に関連付けられた`BusStopPole`は存在しては駄目ですよね？　上のコードのように`foreignKeys`を指定しておけば、`BusStopPole`をデータベースに追加する際に、`busStopName`と同じ値を`name`に持つ`BusStop`が存在することを確認してくれるようになります。これでデータの信頼性が高まって素晴らしい。

インデックスというのは、検索のための索引（インデックス）を作成する仕組みです。インデックスが「ない」場合は、テーブルの行を順に見ていって、条件を満たすものがあるか調べます。Kotlinでいうところの`records.filter { record -> record.busStopName = "日本ユニシス本社前" }`みたいな感じ。レコード数が100件とかなら別にこれでいいですけど、100万件とか1億件とかになると、これでは遅くてやってられません。なので、検索専用のデータ構造を別に作るわけです。B+木というデータ構造を使うことが多いみたいで、B+木を使うとデータ件数が多くてもあっという間に検索できます。Kotlinでも、`Map`を使うと検索がとても早くなりますよね？　あんな感じ。データの追加や更新でインデックスを更新する手間がかかって少しだけ遅くなりますけど、検索が圧倒的に速くなるのでトータルでは素晴らしい。

さて、これで他のエンティティと関係づけられている場合の書き方もわかりましたから、残りもどんどん作っていきます。`Route`はこんな感じ。

~~~ kotlin
package com.tail_island.jetbus.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Route(
    @PrimaryKey
    var id: String,

    var name: String
)
~~~

`BusStop`とほぼ同じですな。続けてこれまでの知見を活用して`RouteBusStopPole`を作る……際に問題となるのは、`RouteBusStopPole`の主キーです。これまで作成してきたエンティティでは公共交通オープンデータセンターのID（である`owl:sameAs`属性の値）をそのまま使えばよかったのですけど、`RouteBusStopPole`ではその手は使えません。でも、独自の重複しないIDを生成して割り当てる処理を作るのは面倒です……。と、そんな場合は`@PrimaryKey`アノテーションの`autoGenerate`を使用できます。こんな感じ。

~~~ kotlin
package com.tail_island.jetbus.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = Route::class, parentColumns = ["id"], childColumns = ["routeId"]), ForeignKey(entity = BusStopPole::class, parentColumns = ["id"], childColumns = ["busStopPoleId"])])
data class RouteBusStopPole(
    @ColumnInfo(index = true)
    var routeId: String,

    var order: Int,

    @ColumnInfo(index = true)
    var busStopPoleId: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
~~~

`@PrimaryKey(autoGenerate = true)`と書けば、主キーには重複しない値が自動で設定されます。なお、上のコードで`var id`をコンストラクタの引数にしていないのは、`RouteBusStopPole`のインスタンスを作成するときに`id`のことを考えたくないから。`data class`にも普通のクラスと同じようにメソッドやプロパティは追加できるので、`{ ... }`で囲んだ中に普通に`var id: Long = 0`でデフォルト値付きのプロパティを追加したというわけです。

以上でエンティティの作成に必要な知識は揃いましたので、同様に`TimeTable`と`TimeTableDetail`、`Bookmark`を定義しましょう。何も新しいことはしていませんので、コードは省略で。どうしても見たい場合は、GitHubのコードを参照してみてください。

## データ・アクセス・オブジェクトの作成

データを入れるエンティティは作成できたので、これを使用してデータベースを操作したい。そのためのオブジェクトがデータ・アクセス・オブジェクト、DAOと省略されるアレですね。さっそく作ってみましょう。`BusStop`用の`BusStopDao`を作ります。

~~~ kotlin
package com.tail_island.jetbus.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BusStopDao {
    @Insert
    fun add(busStop: BusStop)

    @Query("DELETE FROM BusStop")
    fun clear()

    @Query("SELECT * FROM BusStop WHERE name = :name LIMIT 1")
    fun getByName(name: String): BusStop?
}
~~~

データ・アクセス・オブジェクトであることを指示するために`@Dao`アノテーションを追加して、データの追加と更新、削除のメソッドには`@Inert`と`@Update`、`@Delete`を追加します。今回は公共交通オープンデーターセンターから取得したデータを追加するだけなので`@Insert`アノテーションのみ。メソッドの引数はエンティティ・オブジェクトにします。

`@Query`アノテーションは、メソッドが呼ばれたらSQLを実行するようにとの指示です。どんなSQLでもオッケーで、Queryという名前を無視して検索ではないSQLを指定しても大丈夫です。上のコードの`clear()`メソッドがそれですね。公共交通オープンデータセンターのデータと再同期するときのために、`clear()`ではすべてのデータを消す`DELETE FROM BusStop`を実行するように指示しています。

あとは、バス停の名前を指定して`BusStop`を取得するための`getByName()`メソッドを作成しました。メソッドの引数をSQLに渡す場合は、上のコードのように`:パラメーター名`とします。`name = :name`のところですね。`BusStop.name`は主キーなので1件しか存在しないのですけど、念の為`LIMIT 1`を指定しみました。`LIMIT`の分で最適化とかが働くといいなぁ……。そうそう、存在しない`name`が指定された場合に備えるために、戻り値の型は`null`を許容する`BusStop?`にしています。

で、他のデータ・アクセス・オブジェクトもほぼ同様の作り方で作成できるのですけど、エンティティ・オブジェクトで`@PrimaryKey(autoGenerate = true)`とした場合は、少しだけ注意が必要です。というのも、新しいエンティティ・オブジェクトを作成してそれをINSERTした場合、自動生成された主キーが分からないと二度とそのオブジェクトにアクセスできなくなっちゃうんです（検索して取得しようにも、主キー以外で検索したのでは一意になる保証がないですもんね）。というわけで、`@PrimaryKey(autoGenerate = true)`な`RouteBusStopPole`向けの`RouteBusStopPoleDao`は、以下のようなコードになります。

~~~ kotlin
package com.tail_island.jetbus.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RouteBusStopPoleDao {
    @Insert
    fun add(routeBusStopPole: RouteBusStopPole): Long

    @Query("DELETE FROM RouteBusStopPole")
    fun clear()
}
~~~

`@Insert`のメソッドの戻り値を主キーの型にしただけ。これだけで、INSERT時に自動生成された主キーを、戻り値として取得できるようになります。

## データベースを定義する

最後。データベースの定義です。こんな感じ。

~~~ kotlin
package com.tail_island.jetbus.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Bookmark::class, BusStop::class, BusStopPole::class, Route::class, RouteBusStopPole::class, TimeTable::class, TimeTableDetail::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getBookmarkDao(): BookmarkDao
    abstract fun getBusStopDao(): BusStopDao
    abstract fun getBusStopPoleDao(): BusStopPoleDao
    abstract fun getRouteBusStopPoleDao(): RouteBusStopPoleDao
    abstract fun getRouteDao(): RouteDao
    abstract fun getTimeTableDao(): TimeTableDao
    abstract fun getTimeTableDetailDao(): TimeTableDetailDao
}
~~~

`@Database`アノテーションの`entities`でデータベースに含めるエンティティを指定して、`version`で適当にバージョンを指定します。`exportSchema`というのはデータベースを生成するときに使用したスキーマをビルド時に出力してくれる便利機能なのですけど、出力先が指定されていないというビルド・エラーが出たので`false`を設定して出力されないようにしました。

あとは、データ・アクセス・オブジェクトを取得するメソッドを定義するだけです。データ・アクセス・オブジェクトでは`interface`、データベースでは`abstract class`しか定義していませんけど、build.gradleで指定したKotlin Annoataion Processing Tool（kapt）が実体を自動生成してくれますのでご安心ください。

## Roomを呼び出してみる

以上ででRoomの準備は完了です。準備が終わった以上は使ってみたい。前章で作成した公共交通オープンデータセンターからデータを取得するコードを改造して、データベースにデータをキャッシュする処理を書いてみます。

~~~ kotlin
override fun onStart() {
    super.onStart()

    val consumerKey = getString(R.string.consumerKey)
    val webService = Retrofit.Builder().apply {
        baseUrl("https://api.odpt.org")
        addConverterFactory(GsonConverterFactory.create())
    }.build().create(WebService::class.java)

    // データベースのインスタンスを作成します。
    val database = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "jetbus.db").build()

    thread {
        try {
            Log.d("SplashFragment", "Start.")

            // データを削除します。
            database.getTimeTableDetailDao().clear()
            database.getTimeTableDao().clear()
            database.getRouteBusStopPoleDao().clear()
            database.getRouteDao().clear()
            database.getBusStopPoleDao().clear()
            database.getBusStopDao().clear()

            // Webサービスからデータを取得します。
            val busStopPoleJsonArray = getWebServiceResultBody { webService.busstopPole(consumerKey)     } ?: return@thread
            val routeJsonArray       = getWebServiceResultBody { webService.busroutePattern(consumerKey) } ?: return@thread

            // BusStopとBusStopPoleを登録します。
            for (busStopPoleJsonObject in busStopPoleJsonArray.map { it.asJsonObject }.filter { it.get("odpt:operator").asString == "odpt.Operator:Toei" }) {
                val busStop = database.getBusStopDao().getByName(busStopPoleJsonObject.get("dc:title").asString) ?: run {
                    BusStop(
                        busStopPoleJsonObject.get("dc:title").asString,
                        busStopPoleJsonObject.get("odpt:kana")?.asString
                    ).also {
                        database.getBusStopDao().add(it)
                    }
                }

                BusStopPole(
                    busStopPoleJsonObject.get("owl:sameAs").asString,
                    busStop.name
                ).also {
                    database.getBusStopPoleDao().add(it)
                }
            }

            // Routeを登録します。
            for (routeJsonObject in routeJsonArray.map { it.asJsonObject}.filter { it.get("odpt:operator").asString == "odpt.Operator:Toei" }) {
                val route = Route(
                    routeJsonObject.get("owl:sameAs").asString,
                    routeJsonObject.get("dc:title").asString
                ).also {
                    database.getRouteDao().add(it)
                }

                for (routeBusStopPoleJsonObject in routeJsonObject.get("odpt:busstopPoleOrder").asJsonArray.map { it.asJsonObject }) {
                    RouteBusStopPole(
                        route.id,
                        routeBusStopPoleJsonObject.get("odpt:index").asInt,
                        routeBusStopPoleJsonObject.get("odpt:busstopPole").asString
                    ).also {
                        it.id = database.getRouteBusStopPoleDao().add(it)
                    }
                }
            }

            Log.d("SplashFragment", "Finish.")

        } catch (e: IOException) {
            Log.e("SplashFragment", "${e.message}")
        }
    }
}
~~~

少しだけ、解説を。

データベースのインスタンス作成は、`Room.databaseBuilder(context!!, AppDatabase::class.java, "jetbus.db").build()`でやっています。これで、データベースのファイルが無ければエンティティ・オブジェクトの定義に合わせて自動で作り、そのファイルを使用するデータベースが生成されます。

データ・アクセス・オブジェクトで定義したメソッドの実体はRoomが生成してくれますから、たとえばデータを削除しているところでは`database.getTimeTableDatailDao().clear()`のようにごく普通に呼び出せばオッケーです。

あと、公共交通オープンデータセンターのデータでは`BusStopPole`と`BusStop`が一つのJsonObjectになって渡ってきますので、それを分割するために少し面倒なことをしています。`database.getBusStopDao().getByName()`で`BusStop`を取得してみて、もし存在しなければ（`null`が返ってきたなら）、`BusStop`のインスタンスを生成してデータベースに追加しています。エルビス演算子はとても便利！　あと、`also`スコープ関数も。

実行してみます。ログを調べてみると……。

図

はい。成功です。Room簡単ですな。

## データベースをダウンロードして、正しく動いたのか確認してみる

……ごめんなさい。ログに「Finish.」という文字が出たからRoomを正しく使えたと思えってのは、あまりに乱暴でしたね。もう少しきちんと確認しましょう。

スマートフォン上に生成されたデータベースのファイルは、AndroidStudioを使用してダウンロードする事ができます。Android Studioで[Device File Explorer]を開いて、data/data/com.tail_island.jetbus/databases」を開くと、その下に「jetbus.db」というファイルがあります。これ、SQLite3のファイルなんですよ。このファイルを右クリックして、[Save As...]メニューでローカルに保存します。

図

ダウンロードしたデータベースのファイルの中を見て、正しく動作したのかを確認してみます。sqlite3コマンドでデータベースを開いて`SELECT * FROM BusStop LIMIT 10;`を実行して、はい、たしかにバス停が保存されています。この章の前で書いた、出発バス停名と到着バス停名から`Route`を取得するSQLも実行してみます。うん、上りか下りなのかの判別まで含めてうまく行っています。やっぱり、Room簡単ですな。

図

# [Dagger](https://github.com/tail-island/jetbus/tree/dagger)

このあたりで少し冷静になってこれまでに作成したコードを見直してみると、なんだか、`SplashFragment`があまりに汚い……。ちょっとWebサービス呼び出してみようかなってたびに、前準備として以下のコード書くなんてやってられないですよね？

~~~ kotlin
val webService = Retrofit.Builder().apply {
    baseUrl("https://api.odpt.org")
    addConverterFactory(GsonConverterFactory.create())
}.build().create(WebService::class.java)
~~~

でもまぁ、この問題は`WebService`を作る関数を作成すれば解決できそうな気がします。こんな感じ。

~~~ kotlin
fun createWebService(): WebService {
    return Retrofit.Builder().apply {
        baseUrl("https://api.odpt.org")
        addConverterFactory(GsonConverterFactory.create())
    }.build().create(WebService::class.java)
}

createWebService().getWebServiceResultBody { ... }
~~~

ただし`AppDatabase`、テメーはダメだ！　`AppDatabase`を生成するコードを、よく見てみましょう。

~~~ kotlin
val database = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "jetbus.db").build()
~~~

このコードの`databaseBuilder()`メソッドの引数の`requireContext()`がダメ。だって、`requireContext()`は`Fragment`のメソッドなんですよ。だから、`createAppDatabase()`関数を作る場合は、その引数に`Context`を追加してあげなければなりません。そうすると、この関数は`Context`を提供可能な`Fragment`や`Activity`等からしか呼び出せなくなっちゃう。だから`AppDatabase`を使う処理は`Fragment`や`Activity`に書くしかなくて、その結果として`Fragment`や`Activity`に処理のすべてが埋め込まれた汚いコードが出来上がっちゃう……。

うん、Dependency InjectionツールのDaggerを使ってどうにかしましょう！

## Dependency Injection

Dependency Injection（依存性の注入。DIと省略される）はちょっと分かりづらい技術なので、具体的なコードで説明しましょう。`ComponentA`が`ComponentB`に依存している（使用している）とします。

~~~ kotlin
class ComponentA {
    private componentB = new ComponentB()

    fun doSomething() {
        ...
    }
}
~~~

このコードの`ComponentB`が、実はインターネットにあるサービスを呼び出すものでとても遅い場合を考えてみてください。そうなると、単体テストがとても遅くなっちゃう。だから……

~~~ kotlin
interface ComponentB {  // classからinterfaceにします
    ...
}

class ComponentA(componentB: ComponentB) {
    fun doSomething() {
        ...
    }
}

ComponentA(MockComponentB()).doSomething()  // 単体テストではモックを使用して呼び出します
~~~

コードをこんな感じに変更して、単体テスト用の`ComponentB`のモックを用意して、それを使って単体テストします。使用するオブジェクトを実行時に設定することで、オブジェクトを自由に組み合わせられるようにしているわけですな。で、この組み合わせる部分を自動化したり柔軟にしてくれるのが、Daggerを始めとするDependency Injectionツールなんです。

ただ、個人的には、Dependency Injectionはあまり好きではありません。だって、インターフェイスを書くのが面倒なんだもん。動的型付けのプログラミング言語（実行時に型チェックされる言語。JavaScriptとか私が大好きなClojureとか）では`interface`を書かなくてよいから使うし、関数型プログラミング言語（Haskellとか私が愛してやまないClojureとか）なら関数を組み合わせる関数合成を当たり前に使うけど、静的型付けのプログラミング言語（JavaとかKotlinとか。C++は静的型付だけど`template`を使えばコード上では動的に型付けできるから別）でのDependency Injectionはベネフィットよりもコストが大きいと感じちゃう。

## Dependency Injectionツールを敢えて誤用する

Dependency Injectionのメリットは、オブジェクト間の結合度が減ること。オブジェクト間の結合を変更可能にする技術は、静的型付けのオブジェクト指向プログラミング言語ではインターフェイス。でもインターフェイスを書くのは面倒なので、Dependency Injectionは諦めてオブジェクト間の結合度が増えても我慢する。でも、Dependency InjectionツールであるDaggerは使う。これがここまでの私の主張。……無茶苦茶ですがな。

なんでこんな無茶な主張になるのかというと、私はただ単に属性に値を設定する便利ツールとしてDaggerを使っているからです。Adnroidアプリの場合、前述したRoomの`Database`のように`Context`を引数にしないと実現できない処理が多数存在します。これを関数の引数として表現すると呼び出せる場所が限られてしまってよくない。属性にしてコンストラクタで設定する方式も、`Fragment`や`Activity`の生成はAndroidがやるので手を出せないから実現不能です。でも、Daggerを使えば属性を設定できるんですよ。`Context`をDaggerの中で引き回せば、`Fragment`や`Activity`以外でも`Context`を必要とする属性の設定をやりたい放題にできます。

もちろん、インターフェイスを書いていませんから、オブジェクト間の結合度は大きいままです。Dependency Injectionというのは実はデザイン・パターンで、Dependency Injectionツールはそれを実現する手段の一つ。私の意識が高ければツールへの依存性も減らして独自にDependency Injectionパターンを実装するでしょうし、道具が作成された目的を無視して使用するなんてことは絶対にやらないのでしょうけど、残念なことに私は底辺なのでDependency Injectionツールの誤用くらい全然オッケー。苦情は、結合度が異常に高いAndroidのAPIを最初に作った人に言ってください。

ただね、Daggerを使っておけば、本来のDependency Injectionパターンが必要になったときに取り入れやすいと思うんですよ。意識が高い人は、その前準備をしているんだと考えてご容赦ください。

## Daggerの組み込み

まずは、Daggerを組み込むためにbuild.gradleを変更してください。

~~~ gradle
dependencies {
    ...

    kapt 'androidx.room:room-compiler:2.2.4'
    kapt 'com.google.dagger:dagger-compiler:2.24'  // 追加

    implementation fileTree(dir: 'libs', include: ['*.jar'])

    ...

    implementation 'androidx.room:room-ktx:2.2.4'
    implementation 'com.google.dagger:dagger:2.24'  // 追加
    implementation 'com.squareup.retrofit2:retrofit:2.6.1'

    ...
}
~~~

アノテーションによるコード生成が必要ですので、`kapt`を忘れないように気をつけてください。

## Inject

まずは、設定したい属性を作成して、`@Inject`アノテーションを追加します。Daggerは属性の型で設定するインスタンスを決定するのですけど、それでは足りない場合（文字列型の`consumerKey`に文字列を設定するとか）は`@field:Named()`アノテーションを使用します。具体的にはこんな感じ。

~~~ kotlin
class SplashFragment: Fragment() {
    @Inject @field:Named("consumerKey") lateinit var consumerKey: String  // 追加
    @Inject lateinit var webService: WebService                           // 追加
    @Inject lateinit var database: AppDatabase                            // 追加

    ...

    override fun onStart() {
        super.onStart()

        // 不要になるので削除
        // val consumerKey = getString(R.string.consumerKey)
        // val webService = Retrofit.Builder().apply {
        //     baseUrl("https://api.odpt.org")
        //     addConverterFactory(GsonConverterFactory.create())
        // }.build().create(WebService::class.java)
        // val database = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "jetbus.db").build()

        thread {
            try {
                Log.d("SplashFragment", "Start.")

                ...
~~~

## AppModule

次に、注入するオブジェクトを生成するクラスを生成します。名前は`AppModule`にしましょう。`@Module`アノテーションを付加したクラスを作成して、`@Provides`アノテーションを付加した注入するオブジェクトを生成するメソッドを作成します。具体的にはこんな感じ。

~~~ kotlin
package com.tail_island.jetbus

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.tail_island.jetbus.model.AppDatabase
import com.tail_island.jetbus.model.WebService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {
    @Provides
    @Singleton
    fun provideContext() = application as Context

    @Provides
    @Singleton
    @Named("consumerKey")
    fun provideConsumerKey(context: Context) = context.getString(R.string.consumerKey)

    @Provides
    @Singleton
    fun provideDatabase(context: Context) = Room.databaseBuilder(context, AppDatabase::class.java, "jetbus.db").build()

    @Provides
    @Singleton
    fun provideWebService() = Retrofit.Builder().apply {
        baseUrl("https://api.odpt.org")
        client(
            OkHttpClient.Builder().apply {
                connectTimeout(180, TimeUnit.SECONDS)
                readTimeout(180, TimeUnit.SECONDS)
                writeTimeout(180, TimeUnit.SECONDS)
            }.build()
        )
        addConverterFactory(GsonConverterFactory.create())
    }.build().create(WebService::class.java)
}
~~~

上のコードで使用している`@Singleton`は、注入するオブジェクトのインスタンスを1つだけにしたい場合に付加します。今回はたまたま全部についていますけど、異なるインスタンスを使用したい場合は外してください。`@Named`アノテーションは、何を提供すればよいのかが型だけでは判別できない場合向け。`@Inject`のときの`@field:Named`の対になっているわけですな。

で、上のコードで面白いのは、例えば`provideConsumerKey()`メソッドの引数の`context: Context`です。このコードだけを見るとなんだよDagger使っても結局`Context`を引数にしなければならないのかよと感じるんですけど、Daggerはすぐ上の`provideContext()`メソッドが`Context`を提供してくれることを知っています。だから、`consumerKey`を提供するために`provideConsumerKey()`メソッドを実行するときには、Daggerが自動的に`provideContext()`を実行して引数を準備してくれるんです（さらにすごいことに、Lifecycleのところで使用しますけど、引数のすべてを`@Provides`できるコンストラクタを持っているクラスなら何も書かなくても提供できます）。というわけで、もうこれで`Context`をどうやって取得しよう問題はクリア！

まぁ、`AppModule`生成の引数に`Application`が含まれているので、問題を先送りしただけなんだけどな。

## App

というわけで、`Application`を引数にして`AppModule`を生成する処理を追加してみましょう。そのために、`Application`を継承した`App`クラスを作成します。

~~~ kotlin
package com.tail_island.jetbus

import android.app.Application

class App: Application() {
    // DaggerのComponentを取得するためのプロパティ
    lateinit var component: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        // 先程作成したAppModuleを使用して、DaggerのComponentを作成します
        component = DaggerAppComponent.builder().apply {
            appModule(AppModule(this@App))
        }.build()
    }
}
~~~

そのうえで、作成した`App`が使用されるようにAndroidManifest.xmlを修正します。

~~~ xml
<?xml version="1.0" encoding="utf-8"?>
<manifest
    xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.tail_island.jetbus">

    <uses-permission android:name="android.permission.INTERNET" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:name=".App"  <!-- 追加 -->
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">

        <activity android:name=".MainActivity" android:theme="@style/AppTheme.NoActionBar">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

    </application>

</manifest>
~~~

修正は、`android:name=".App"`属性の追加だけ。これで、標準の`Application`ではなく、先程作成した`App`が使用されるようになります。

## AppComponent

でも、先程の`App`のコード中にでてきたDaggerの`Component`っていったい何なんでしょ？　このDaggerの`Component`は、`Module`から取得したオブジェクトのインスタンスを注入するオブジェクトで、`@Component`アノテーションを付加した`interface`を定義するだけで自動生成されます。この`inreface`を定義しましょう。

~~~ kotlin
package com.tail_island.jetbus

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(splashFragment: SplashFragment)
}
~~~

`AppComponent`という名前で`interface`を作ると、Daggerが`DaggerAppComponent`を自動生成してくれます。先程の`App`の中でDaggerのドキュメントの中に存在しない`DaggerAppComponent`を使えていたのは、自動生成されるからなんですね。先程の`App`のコードを書いているときに`DaggerAppComponent`がないというエラーがでて不安になった方、ごめんなさい。`interface AppCompoent`を作成して一度ビルドすれば、`DaggerAppComponent`がないというエラーは解消されます。

あと、今回は`Application`経由でDaggerの`Component`を取得するので（その結果として1つだけになるから）なくてもよいのですけど、Daggerの`Component`は1つであって欲しいので念の為に`@Singleton`アノテーションを付加しておきます。宣言するメソッドは、依存性を注入する対象を引数にしたメソッドだけ。これで、Daggerを使うための準備は完了です。面倒な作業ですけど、機械作業なので難しくはないから我慢してください。

## 依存性を注入する

準備が完了したので、`SplashFragment`に依存性を注入しましょう。SplashFragment.ktを開いて、以下の修正をします。

~~~ kotlin
class SplashFragment: Fragment() {
    ...

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Daggerを使用して、依存性を注入します
        (requireActivity().application as App).component.inject(this)  // 追加

        return FragmentSplashBinding.inflate(inflater, container, false).apply {
            bookmarksButton.setOnClickListener {
                findNavController().navigate(SplashFragmentDirections.splashFragmentToBookmarksFragment())
            }
        }.root
    }
~~~

`requireActivity()`で取得した`Activity`を経由して`Application`を取得して、そこからDaggerの`Component`を取得して自分自身を引数に`inject()`します……って、これ、結局`Context`の代わりに`Application`が必要になっただけじゃん！　Daggerで楽するために単純作業に耐えてきたのに、ベネフィットは複数の属性を1行で設定できるようになっただけ？

こんなの絶対おかしいよ……とどこぞの魔法少女のように絶望したかもしれませんけど、安心してください、幸いなことにDaggerには救いがあります。どう救われるのかは、次に説明するLifecycleの中で！

# [Lifecycle](https://github.com/tail-island/jetbus/tree/lifecycle)

と、かなり強引な引きで始めてみたLifecycleなのですけど、ごめんなさい、やたらと書くことが多いので、Daggerの世界が救われるのはこの章の最後です。

考えてみれば、Androidアプリの開発が大変だった一番大きな理由は、本稿の最初の方で述べたライフサイクルの管理が大変なためなんです。やたらと複雑な`Activity`のライフサイクルから、もうとにかく逃げたい。そのために状態をViewModelに分離したというわけ。なので、ViewModelとはなんぞやというアーキテクチャーの話をしなければなりません。あと、`Activity`や`Fragment`の状態の変化に合わせて正しく動作するには、データの変更を監視する方式が定まっていないとダメ。そうでなければ、状態の変化に対応するための制御を入れられないですもんね。だから、その手段であるLiveDataについての説明も必要です。で、LiveDataは前述したRoomといい感じに協調して動作するように作られているのでデータベースの話まで戻らなくちゃなりません（Roomで不足しているように感じた機能が、LiveDataと組み合わせると不足していたのではなくて無いことこそが正しいとなるのでとても面白いですよ）。

覚悟してください。この章はとても長いですよ……。

## MVVM（Model-View-ViewModel）アーキテクチャとは？

Android Jetpackでは、MVVMアーキテクチャを使うことが推奨されています。このMVVMアーキテクチャを理解するには、その前段であるMVC（Model-View-Controller）アーキテクチャの知識があると楽。しかも、Android JetpackのMVVMアーキテクチャは、普通のMVVMアーキテクチャとは少し違っていたりします。

というわけで、MVCアーキテクチャ、MVVMアーキテクチャ、Android JetpackのMVVMアーキテクチャの順に説明させてください。

### MVC（Model-View-Controller）アーキテクチャ

大昔にゼロックス社のパロアルト研究所が開発したSmalltalkという伝説的なプログラミング言語があって（今もあって熱烈なファンがいるけど）、このSmalltalkはパロアルト研究所が開発したAltoというコンピューターのOS（Operating System）でした。プログラミング言語がOSなんておかしいと思ってしまった人は、電源を入れるとMSX-BASICのインタープリターが起動するMSXでコンピューターを始めた私に謝ってください。特定のプログラミング言語を動かすためのコンピューターって、大昔にはけっこうあったんですよ。

で、このAltoってのは当時にしてはものすごく先進的なコンピューターで、マウスがついていてGUI（Graphical User Interface）を持っていました。もちろん世界初です。この世界初のGUIをどうにかしていい感じに開発できないかなぁと考えて作られたのが、MVCアーキテクチャなんです。

図

Controllerは入力機器（マウスとかキーボードとか）からの入力を監視し、マウス・クリックとかキーボードのAが押されたとかのイベントをもとに、Modelというデータとデータ操作の手続きを管理するオブジェクトにメッセージを送り（メソッドを呼び出し）ます。Modelのデータが変更されたことはViewに通知され（Observerパターン）、通知を受け取ったViewは自分自身を置き換えて出力する。以上がMVCアーキテクチャなんです。

MVCアーキテクチャには良いところもあったのでしょうけど、GUIが複雑になってくると、GUIの入力と出力を分けるのは無理がある（たとえばボタンがタップされたときのアニメーションを、ControllerとModel経由でやるのは無駄が多すぎる）ということになりました。だから、Androidアプリ開発ではControllerは分割されていません。

ちなみに、このViewとControllerが一体化した場合のアーキテクチャがDocument-Viewアーキテクチャだったりします。代表例はMFC（Microsoft Foundation Classes）、懐かしいなぁ。

ただ、世の中には用語を適当に使う人たちがいるので、Webアプリケーションの開発でもMVCアーキテクチャという言葉が使われるようになってしまったんですよ……。

WebアプリケーションのMVCアーキテクチャはSmalltalkのMVCとは*無関係*で、一般的にModelはO/Rマッピング・ツールでマッピングされたレコードを表現するオブジェクトです（もちろん普通のクラスが含まれていても構いません）。Controllerはリクエストを受け取ってModelを更新/取得してViewが必要とするデータを用意し、ViewはControllerから渡されたデータをHTMLに変換します。これがWebアプリケーションの場合のMVCアーキテクチャなんですけど、勝手に名乗っているだけな上に無関係なので、今回はこのMVCアーキテクチャは忘れてください。

### MVVM（Model-View-ViewModel）アーキテクチャ

MVVMアーキテクチャというのは、.NET Framework 3.0のWPF（Windows Presentation Foundation）やSilverlightのために考案されたアーキテクチャです。理屈の上ではModelとViewだけで良さそうなのですけど、WPFやSilverlightではXAMLというViewをXMLで定義する言語を持っていて、この言語はとても高機能で素晴らしいのですけど、XAMLだけでViewを作成した場合は、Model側にViewのためのコードを書く必要がありました。これではModelとViewに分割した意味がありませんから、間にViewModelを挟んで、Model-View-ViewModelとなりました。MVVMアーキテクチャでは、メソッドの呼び出しは片方向（View→ViewModel→Model）だけで、逆の方向は変更の通知で情報を伝えることになっています（ModelとViewModelの間は返り値でもOK）。

図

さて、ViewをレイアウトのXMLとソース・コードの合わせ技で実装するAndroidアプリ開発ではViewModelは不要に感じられるのですけど、AndroidのViewである`Activity`や`Fragment`には状態を持てないという別の制約がありました。このViewの状態を管理するために、ViewModelを使用するというわけ。必要に迫られて仕方なく、という感じのアーキテクチャなんですな。

### Android JetpackでのModel-View-ViewModelアーキテクチャ

Android Jetpackでは、もう一つ層を追加することを提案しています。それがRepositoryです。

図

Androidが動作するスマートフォンやタブレット、ウェアラブル・デバイスは、インターネットとの親和性が高いデバイスです。だから、我々が作成するアプリもインターネットと頻繁に通信する可能性が高い。しかもローカルのファイルやデータベースだって使用するわけで、だから、モデルはWebサービスに基づく場合とデータベースやファイルに基づく場合がありえます。これらがバラバラのままだと管理が大変になってしまうので、Repositoryという層を追加して一本化してViewModelはRepositoryだけに依存しましょうという感じ。

で、本来のMVVMのModelはビジネス・ロジックを含む分厚い層で、Android Jetpackのドキュメントでも図だけはそんな感じに書いてあるのですけど、実際としては、ただのRoomによるDAO（Data Access Object）とEntity、Retrofit2によるWebサービスになります（たぶん）。だって、ModelというのはReposirotyでラップできるようなレベルなんですしね。後述するLiveDataとRoomを組み合わせれば、データベースの変更がViewModelに通知されて楽チンだし。結果としてViewModelは少し大きくなるけど、Android JetpackならViewModel上にロジックを書くのがやたらと楽だしね。

MVVMアーキテクチャ原理派の方々はこんなやり方は認めないのでしょうけど、楽にコードを書けるのだから私的には全然オッケー。

## LiveData

というわけで長いMVVMアーキテクチャの説明が終わったのでさっそくViewModelを作る……前に、MVVMアーキテクチャではViewModelからViewへの情報の伝達は通知で実現されることになっていたことを思い出してください。

しかも、考えたくもないしLiveDataを使えば考える必要はほとんどなくなるのですけど、`Activity`や`Fragment`は複雑なライフサイクルを持っているので、単純にObserverパターンを使うと通知が来たときにはすでに`Activity`や`Fragment`が破棄されていて処理を実行したら即クラッシュなんて可能性もあるんですよ。

これらの課題を、LiveDataでサクサク解決しちゃいましょう。

### `MutableLiveData`

と思ったのですけど、さて、困りました。LiveDataのオブジェクトはViewModelのプロパティとするのが普通なのですけれど、まだViewModelの作り方を説明できていません……。とりあえずは、`Fragment`から参照できて`Activity`や`Fragment`よりも生存期間が長い`App`に置くことにしましょう。出発バス停の名前を表現する`MutableLiveData`を作成します。

~~~ kotlin
package com.tail_island.jetbus

import android.app.Application
import androidx.lifecycle.MutableLiveData

class App: Application() {
    lateinit var component: AppComponent
        private set

    val departureBusStopName = MutableLiveData<String>()

    ...
~~~

`MutableLiveData`が作成できましたので、監視してみましょう。バス接近情報を表示する`BusApproachesFragment`では確実に出発バス停の情報が必要になるでしょうから、`BusApproachesFragment`に配置してみます。まずはfragment_bus_approaches.xmlの変更。

~~~ xml
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    tools:context=".DepartureBusStopFragment">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <TextView
            android:id="@+id/departureBusStopNameTextView"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:layout_marginStart="16dp"
            android:layout_marginEnd="16dp"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"/>

    </androidx.constraintlayout.widget.ConstraintLayout>

</layout>
~~~

続いて、BusApproachesFragment.ktの変更です。

~~~ kotlin
package com.tail_island.jetbus

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.tail_island.jetbus.databinding.FragmentBusApproachesBinding
import kotlin.concurrent.thread

class BusApproachesFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentBusApproachesBinding.inflate(inflater, container, false).apply {
            (requireActivity().application as App).departureBusStopName.observe(viewLifecycleOwner, Observer { departureBusStopNameValue ->
                Log.d("BusApproachesFragment", "departureBusStopName.observe()")
                departureBusStopNameTextView.text = departureBusStopNameValue
            })

            thread {
                Thread.sleep(5000)

                (requireActivity().application as App).departureBusStopName.postValue("日本ユニシス本社前")
                Log.d("BusApproachesFragment", "MutableLiveData.postValue()")
            }
        }.root
    }
}
~~~

`observe()`の引数の`Observer { ... }`は、抽象メソッド一つだったらラムダ式から変換してやる（SAM変換）というKotlinの機能を使用しています。ラムダ式でいいんだったら前に付いている`Observer`は何なんだとか、どうして`(...)`の内側に入っているんだこれまでの書き方と違うじゃないかという疑問は、`observe()`にはいくつもバージョンがあるのでそのどれなのかを指定しなければならないから。普段と書き方が違うので面倒ですけど、普通の書き方をするとコンパイル・エラーになるので発見も書き換えも容易ですのでまぁいいかなぁ。

で、これで作業終了です。`MutableLiveData`に値を設定するには、メイン・スレッドからの場合は`value`プロパティ、他のスレッドからの場合は`postValue()`メソッドを使用します。スレッドを作成して5秒経ったら、私の両親が「お前をまだクビにしてないんだから度量が大きい会社だな」と評価した会社の前にあるバス停が設定され、画面に表示されます。

で、ここで試していただきたいのですけど、バス接近情報を表示する画面に遷移したら、5秒経つ前にホーム画面を表示してアプリをバックグラウンドに移動させてみてください。バックグラウンドに移ったので画面を更新する必要はなくて、だから`Observer`の呼び出しは無駄です。LiveDataはこのことを知っていて、しかも、`Fragment`がどのような状態にあるのかを`viewLifecycleOwner`を経由して知ることができるので、変更を通知しなくなるんです。その証拠に、ほら、logcatを見てください「departureBusStopName.observe()」が表示されていないでしょ？　で、アプリをフォアグランドに戻すと、すぐにlogcatに「departureBusStopName.observe()」が表示されて、私の会社の前にあるバス停の名前が画面に表示される。 というわけで、ほら、LiveDataのおかげで`Activity`や`Fragment`のライフサイクルがどうなっているのかを考えながらプログラミングする手間はほぼなくなりました！

### LiveDataとRoomを組み合わせる

まだまだLiveDataはこんなもんではありません。RoomとLiveDataを組みわせるともっとすごいことができるんです。

以前の章でRoomを使ったときのことを思い出してみましょう。Roomはメイン・スレッドからは呼び出せなくて、とても面倒だった記憶が蘇ってきました（SplashFragment.ktを参照してください）。

この問題、LiveDataを使うととても簡単に解消できるんですよ。出発バス停と路線がつながっている到着バス停の一覧を取得する処理を考えてみましょう。まずは、`BusStopDao`に到着バス停を取得するメソッドを追加してみます。

~~~ kotlin
package com.tail_island.jetbus.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BusStopDao {
    ...

    @Query(
        """
            SELECT DISTINCT ArrivalBusStop.*
            FROM BusStop AS ArrivalBusStop
            INNER JOIN BusStopPole AS ArrivalBusStopPole ON ArrivalBusStopPole.busStopName = ArrivalBusStop.name
            INNER JOIN RouteBusStopPole AS ArrivalRouteBusStopPole ON ArrivalRouteBusStopPole.busStopPoleId = ArrivalBusStopPole.id
            INNER JOIN Route ON Route.id = ArrivalRouteBusStopPole.routeId
            INNER JOIN RouteBusStopPole AS DepartureRouteBusStopPole ON DepartureRouteBusStopPole.routeId = Route.id
            INNER JOIN BusStopPole AS DepartureBusStopPole ON DepartureBusStopPole.id = DepartureRouteBusStopPole.busStopPoleId
            INNER JOIN BusStop AS DepartureBusStop ON DepartureBusStop.name = DepartureBusStopPole.busStopName
            WHERE DepartureBusStop.name = :departureBusStopName AND ArrivalBusStop.name <> :departureBusStopName
            ORDER BY ArrivalBusStop.phoneticName
        """
    )
    fun getObservablesByDepartureBusStopName(departureBusStopName: String): LiveData<List<BusStop>>
}
~~~

SQLが複雑に見えますけど、BusStop（到着）→BusStopPole（到着）→RouteBusStopPole（到着）→Route→RouteBusStopPole（出発）→BusStopPole（出発）→BusStop（出発）と`INNER JOIN`で辿っているだけ。Roomの解説の章でダウンロードしたSQLite3のデータベース・ファイルを使って実際に試しながらやれば、そんなに難しくないはず。

で、このコードの重要なところは、`getObservablesByDepartureBusStopName()`メソッドの返り値の「型」です。普通にRoomを使う場合の返り値の型は`List<BusStop>`になるのですけど、上のコードでは`LiveData<List<BusStop>>`になっています。この小さな変更だけで、LiveData対応のデータ・アクセスのメソッドが生成されるんです。

次の作業は、`App`へのRoomで作成するLiveData型のプロパティ追加なのですけど、そのためには`AppDatabase`が必要なので、まずは`AppCompoenent`に`fun inject(app: App)`を追加します。そのうえで、`App`を以下に修正してください。

~~~ kotlin
package com.tail_island.jetbus

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tail_island.jetbus.model.AppDatabase
import com.tail_island.jetbus.model.BusStop
import javax.inject.Inject

class App: Application() {
    lateinit var component: AppComponent
        private set

    @Inject lateinit var database: AppDatabase

    val departureBusStopName = MutableLiveData<String>()
    val arrivalBusStops: LiveData<List<BusStop>> by lazy { database.getBusStopDao().getObservablesByDepartureBusStopName("日本ユニシス本社前") }

    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent.builder().apply {
            appModule(AppModule(this@App))
        }.build()

        component.inject(this)
    }
}
~~~

`arrivalBusStops`プロパティで使っている`by lazy`は、必要になるまでプロパティの初期化を遅らせる機能で、移譲プロパティと呼ばれる機能の一つです。`database`が注入される前に`database.getBusStopDao()`するわけにはいかないですもんね。

最後は、`BusApproachesFragment`の修正です。`MutableLiveData`の場合と同様に、`observe()`メソッドを呼び出して変更に合わせて画面を変更するようにしておきます。

~~~ kotlin
package com.tail_island.jetbus

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.tail_island.jetbus.databinding.FragmentBusApproachesBinding
import kotlin.concurrent.thread

class BusApproachesFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentBusApproachesBinding.inflate(inflater, container, false).apply {
            (requireActivity().application as App).departureBusStopName.observe(viewLifecycleOwner, Observer { departureBusStopNameValue ->
                Log.d("BusApproachesFragment", "departureBusStopName.observe()")
                departureBusStopNameTextView.text = departureBusStopNameValue
            })

            (requireActivity().application as App).arrivalBusStops.observe(viewLifecycleOwner, Observer { arrivalBusStopsValue ->
                Log.d("BusApproachesFragment", "arrivalBusStops.observe(), ${arrivalBusStopsValue.size}")
                arrivalBusStopNamesTextView.text = arrivalBusStopsValue.map { it.name }.joinToString("\n")
            })

            thread {
                Thread.sleep(5000)

                (requireActivity().application as App).departureBusStopName.postValue("日本ユニシス本社前")
                Log.d("BusApproachesFragment", "MutableLiveData.postValue()")
            }
        }.root
    }
}
~~~

Kotlinは関数型プログラミングのテクニックが使えてとても便利ですな。`map`で名前だけのリストに変換して、`joinToString()`で一つの文字列にまとめています。

プログラムが完成したので、早速実行してみます。手早くバス接近情報の画面まで遷移してlogcatを見てみると、何度も何度も「arrivalBusStops.observe()」と表示されて、かなり時間が経ってから、少しづつ到着バス停の候補が画面に表示されていきます。え？　どうしてこうなった？

あの、実はこれこそが、LiveDataとRoomを組み合わせるとできるようになる本当にすごいことなんです。LiveDataとRoomを組み合わせた場合は、データベースに変更があると通知がやってくるんですよ！

前にやった作業なのですっかり忘れていましたけど、我々は`SplashFragment`の中でWebサービスを呼び出して、取得したデータでデータベースを書き換える処理を実装していました。その処理はLifecycleを使わずにスレッドとして実装しましたから、SplashFragmentが終了しても動き続けます。なので、手早くバス接近情報の画面まで移動すると、まだデータベースの更新処理が動いているんですよ。で、データが更新されるたびに通知が来るので、何度も何度も「arrivalBusStops.observe()」と表示され、少しづつ到着バス停の候補が画面に表示されるという動きになったわけですな。

というわけで、LiveDataとRoomを組み合わせれば、MVVMのModelからViewModelへの変更の通知も実現できるんですよ。素晴らしい！

### `Transformations`

でもちょっと待って。先程のコードで`getObservablesByDepartureBusStopName()`を呼び出すときの引数をソース・コードに直打ちしていなかった？　それでは実用に耐えないのでは？

はい。おっしゃるとおり。`departureBusStopName`が変更されたらその値で`getObservablesByDepartureBusStopName()`が呼ばれるようになっていないとなりません。もちろん、Android Jetpackはそのための機能を提供していて、それが`Transformations`です。さっそく`Transformations`を使ってみましょう。

`App`を以下のように修正します。

~~~ kotlin
package com.tail_island.jetbus

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tail_island.jetbus.model.AppDatabase
import javax.inject.Inject

class App: Application() {
    lateinit var component: AppComponent
        private set

    @Inject lateinit var database: AppDatabase

    val departureBusStopName = MutableLiveData<String>()

    val arrivalBusStops = Transformations.switchMap(departureBusStopName) { arrivalBusStopNameValue ->
        database.getBusStopDao().getObservablesByDepartureBusStopName(arrivalBusStopNameValue)
    }

    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent.builder().apply {
            appModule(AppModule(this@App))
        }.build()

        component.inject(this)
    }
}
~~~

`Transformations`の`switchMap()`メソッドは、１つ目の引数で指定した`LiveData`が変更になったら、2つ目の引数のラムダ式の結果がセットされる`LiveData`を返します。なお、今回はラムダ式が`LiveData`を返すので`switchMap()`を使用しましたが、`Transformations`には、`LiveData`ではない返り値を使う場合向けの`map()`というメソッドもありますのでいい感じに使い分けてください。

で、この`Transformations`を使うと、Roomにテーブルとテーブルの間を辿る機能がないことが気にならなくなります。たとえば注文と注文明細を表示するような場合は、注文と注文明細を`Transformations`でつないで、注文と関係づけられた注文明細を取得するデータ・アクセス・オブジェクトのメソッドを書けばいいんですから。というわけで、Roomは前に書いたような「こういうのでいいんだよ。こういうので」と表現されるような機能が貧弱なO/Rマッピング・ツールではなくて、不要な機能がついていないとても洗練された最高に出来が良いO/Rマッピング・ツールなんです。もし「Roomは機能が貧弱で」とのたまうRoomの説明を書いていたときの私みたいな人がいたら、分かってないなぁと鼻で笑ってやってください。

最後。せっかく`departureBusStopName`が変更になったら`arrivalBusStops`が変更されるようになったのですから、`BusApproachesFragment`を修正して画面も変更されるようにしましょう。

~~~ xml
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    tools:context=".DepartureBusStopFragment">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <TextView
            android:id="@+id/departureBusStopNameTextView"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:layout_marginStart="16dp"
            android:layout_marginEnd="16dp"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"/>

        <TextView
            android:id="@+id/arrivalBusStopNamesTextView"
            android:layout_width="0dp"
            android:layout_height="0dp"
            android:layout_marginTop="16dp"
            android:layout_marginBottom="16dp"
            app:layout_constraintTop_toBottomOf="@id/departureBusStopNameTextView"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintStart_toStartOf="@id/departureBusStopNameTextView"
            app:layout_constraintEnd_toEndOf="@id/departureBusStopNameTextView" />

    </androidx.constraintlayout.widget.ConstraintLayout>

</layout>
~~~

~~~ kotlin
package com.tail_island.jetbus

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.tail_island.jetbus.databinding.FragmentBusApproachesBinding
import kotlin.concurrent.thread

class BusApproachesFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentBusApproachesBinding.inflate(inflater, container, false).apply {
            (requireActivity().application as App).departureBusStopName.observe(viewLifecycleOwner, Observer { departureBusStopNameValue ->
                Log.d("BusApproachesFragment", "departureBusStopName.observe()")
                departureBusStopNameTextView.text = departureBusStopNameValue
            })

            (requireActivity().application as App).arrivalBusStops.observe(viewLifecycleOwner, Observer { arrivalBusStopsValue ->
                Log.d("BusApproachesFragment", "arrivalBusStops.observe(), ${arrivalBusStopsValue.size}")
                arrivalBusStopNamesTextView.text = arrivalBusStopsValue.map { it.name }.joinToString("\n")
            })

            thread {
                Thread.sleep(5000)

                (requireActivity().application as App).departureBusStopName.postValue("日本ユニシス本社前")
                Log.d("BusApproachesFragment", "MutableLiveData.postValue()")

                Thread.sleep(5000)

                (requireActivity().application as App).departureBusStopName.postValue("深川第八中学校前")
            }
        }.root
    }
}
~~~

5秒たったら`departureBusStopName`を「日本ユニシス本社前」に設定して、また5秒たったら`departureBusStopName`を私のアパートの最寄りバス停である「深川第八中学校前」に変更するわけですな。`SplashFragment`でlogcatに「Finish.」が表示されるまで待って（そうしないとデータベースの更新と`departureBusStopName`の更新が合わさって変な動きになっちゃう）、バス接近情報の画面に遷移します。

5秒後に「日本ユニシス本社前」とつながっているバス停の一覧が表示されて、さらに5秒たったら画面が「深川第八中学校前」とそこにつながっているバス停の一覧に変更されたでしょ？　はい、これで終わり。`Transformations`は実に便利ですな。

### `MediatorLiveData`

でも、あれ、ユーザーIDとパスワードが入力されたらログインして認証済みかどうかの`LiveData`を更新するような場合、複数の値を監視しなければならない場合はどうするのでしょうか？　今回のアプリだと、出発バス停と到着バス停からルートを取得するような場合です。`Transformations`は1つの`LiveData`しか監視できないですよね？　それでは、要件を満たせません。

というわけで、そんな場合は`MediatorLiveData`を使いましょう。出発バス停の名称と到着バス停の名称からルートを取得する処理を作りたいので、まずはルートを取得するメソッドを`RouteDao`に追加します。

~~~ kotlin
package com.tail_island.jetbus.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RouteDao {
    ...

    @Query(
        """
            SELECT Route.*
            FROM BusStopPole AS ArrivalBusStopPole
            INNER JOIN RouteBusStopPole AS ArrivalRouteBusStopPole ON ArrivalRouteBusStopPole.busStopPoleId = ArrivalBusStopPole.id
            INNER JOIN Route ON Route.id = ArrivalRouteBusStopPole.routeId
            INNER JOIN RouteBusStopPole As DepartureRouteBusStopPole ON DepartureRouteBusStopPole.routeId = Route.id
            INNER JOIN BusStopPole AS DepartureBusStopPole ON DepartureBusStopPole.id = DepartureRouteBusStopPole.busStopPoleId
            WHERE ArrivalRouteBusStopPole.'order' > DepartureRouteBusStopPole.'order' AND DepartureBusStopPole.busStopName = :departureBusStopName AND ArrivalBusStopPole.busStopName = :arrivalBusStopName
        """
    )
    fun getObservablesByDepartureBusStopNameAndArrivalBusStopName(departureBusStopName: String, arrivalBusStopName: String): LiveData<List<Route>>
}
~~~

そのうえで、`App`のコードを以下に変更します。

~~~ kotlin
package com.tail_island.jetbus

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tail_island.jetbus.model.AppDatabase
import com.tail_island.jetbus.model.Route
import javax.inject.Inject

class App: Application() {
    lateinit var component: AppComponent
        private set

    @Inject lateinit var database: AppDatabase

    val departureBusStopName = MutableLiveData<String>()

    val arrivalBusStops = Transformations.switchMap(departureBusStopName) { arrivalBusStopNameValue ->
        database.getBusStopDao().getObservablesByDepartureBusStopName(arrivalBusStopNameValue)
    }

    val arrivalBusStopName = Transformations.map(arrivalBusStops) { arrivalBusStopsValue ->
        arrivalBusStopsValue[0.until(arrivalBusStopsValue.size).random()].name
    }

    val routes = MediatorLiveData<List<Route>>().apply {
        var source: LiveData<List<Route>>? = null

        fun update() {
            val departureBusStopNameValue = departureBusStopName.value ?: return
            val arrivalBusStopNameValue   = arrivalBusStopName.value   ?: return

            source?.let {
                removeSource(it)
            }

            source = database.getRouteDao().getObservablesByDepartureBusStopNameAndArrivalBusStopName(departureBusStopNameValue, arrivalBusStopNameValue).also {
                addSource(it) { sourceValue ->
                    value = sourceValue
                }
            }
        }

        addSource(departureBusStopName) { update() }
        addSource(arrivalBusStopName)   { update() }
    }

    ...
~~~

うぉ、面倒臭そう……なので、少し解説を。

`departureBusStopName`（出発バス停名称）が変更になったら、`arrivalBusStops`（到着バス停のリスト）を設定するところまでは前にやりました。今回は`arrivalBusStopName`（到着バス停名称）も必要なので、`Transformations`の`map()`メソッドを使用して`arrivalBusStops`が変更になったらその中の一つをランダムに選ぶようにしました。

で、`routes`（ルートのリスト）が問題の`MediatorLiveData`です。`MediatorLiveData`では、監視対象の`LiveData`を`addSource()`メソッドで追加できます。`addSource()`メソッドの2つ目の引数はラムダ式で、監視対象が変更になった場合に実行する処理です。今回は、その直前に定義している`update()`関数を呼び出しています。で、`MediatorLiveData`の値を変更したい場合は、`value`プロパティに値を設定すればよくて、監視対象の`LiveData`の値は`LiveData`の`value`プロパティで取得できるのですけど、残念なことに`RouteDao`に定義したのは`LiveData<List<Route>>`を返すメソッドですから、そのままでは`value`に設定できません。

だから、`getObservablesByDepartureBusStopNameAndArrivalBusStopName()`の返り値を格納するための`source`変数を作成して、設定と同時に`also`で`addSource()`して`value`に値を設定するようにしています。`source`が複数になると動作がおかしくなりますから、`source?.let`で過去に設定した`source`がある場合は`removeSource()`しています。

あと、`update()`の中でエルビス演算子（`:?`）を使用しているのは、`LiveData`のテンプレート引数が`null`を許容しない型であったとしても、値がまだ設定されていない場合は`value`プロパティの値が`null`になってしまうためです。出発バス停名称と到着バス停名称の両方が揃った場合に初めて処理を実施するというわけですな。

……とまぁ、異常に複雑で、これを少しでも簡単にするには「`RouteDao`に`LiveData`を返さないメソッドを定義する」という手があるのですけど、場合によって作り方を変更するのは混乱の元になるので避けたい。まぁ、複雑だとはいっても毎回同じなのでいつかは見慣れるでしょうから、ごめんなさい、このままで。こーゆーもんなんだと無理に飲み込んでください。誰かライブラリ化してくれないかなぁ。KotlinにLISPやRustのマクロがあれば、こんな場合にも簡単にライブラリ化できるのに……。

ともあれ、面倒くさいのはここまでで終わり。あとは、これまでと同じです。レイアウトのXMLをいい感じに修正して……

~~~ xml
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    tools:context=".DepartureBusStopFragment">

    <data>
        <variable name="app" type="com.tail_island.jetbus.App" />
    </data>

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <TextView
            android:id="@+id/departureBusStopNameTextView"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:layout_marginStart="16dp"
            android:layout_marginEnd="16dp"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent" />

        <TextView
            android:id="@+id/arrivalBusStopNameTextView"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            app:layout_constraintTop_toBottomOf="@id/departureBusStopNameTextView"
            app:layout_constraintStart_toStartOf="@id/departureBusStopNameTextView"
            app:layout_constraintEnd_toEndOf="@id/departureBusStopNameTextView" />

        <TextView
            android:id="@+id/routeNamesTextView"
            android:layout_width="0dp"
            android:layout_height="0dp"
            android:layout_marginTop="16dp"
            android:layout_marginBottom="16dp"
            app:layout_constraintTop_toBottomOf="@id/arrivalBusStopNameTextView"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintStart_toStartOf="@id/arrivalBusStopNameTextView"
            app:layout_constraintEnd_toEndOf="@id/arrivalBusStopNameTextView" />

    </androidx.constraintlayout.widget.ConstraintLayout>

</layout>
~~~

`Fragment`側も修正します。

~~~ kotlin
package com.tail_island.jetbus

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.tail_island.jetbus.databinding.FragmentBusApproachesBinding
import kotlin.concurrent.thread

class BusApproachesFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentBusApproachesBinding.inflate(inflater, container, false).apply {
            (requireActivity().application as App).departureBusStopName.observe(viewLifecycleOwner, Observer { departureBusStopNameValue ->
                Log.d("BusApproachesFragment", "departureBusStopName.observe()")
                departureBusStopNameTextView.text = departureBusStopNameValue
            })

            (requireActivity().application as App).arrivalBusStopName.observe(viewLifecycleOwner, Observer { arrivalBusStopNameValue ->
                arrivalBusStopNameTextView.text = departureBusStopNameValue
            })

            (requireActivity().application as App).routes.observe(viewLifecycleOwner, Observer { routesValue ->
                routeNamesTextView.text = routesValue.map { it.name }.joinToString("\n")
            })

            thread {
                Thread.sleep(5000)

                (requireActivity().application as App).departureBusStopName.postValue("日本ユニシス本社前")
                Log.d("BusApproachesFragment", "MutableLiveData.postValue()")

                Thread.sleep(5000)

                (requireActivity().application as App).departureBusStopName.postValue("深川第八中学校前")
            }
        }.root
    }
}
~~~

これで出発バス停の名称と到着バス停の名称とその2つのバス停をつなぐルートの一覧が表示されるようになりました。あ、このアプリを動かすとルートが複数表示されるのは、ルートの出発バス停や到着バス停が異なる場合があるためです。

これでLiveDataの説明は完了です。`MediatorLiveData`の使い方が少し面倒でしたけど、全体で見ればとても便利でしょ？　長かった説明が終わってよかった……。

### データ・バインディング

まだだ！　まだ終わらんよ！　だって、`Fragment`のコードで`observe()`するのって面倒じゃあないですか？　この処理って、データ・バインディングを使うととても簡単になるんです。

さっそくやってみましょう。データ・バインディングは、レイアウトのXMLに記載します。

~~~ xml
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    tools:context=".DepartureBusStopFragment">

    <data>
        <variable name="app" type="com.tail_island.jetbus.App" />
    </data>

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <TextView
            android:id="@+id/departureBusStopNameTextView"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:layout_marginStart="16dp"
            android:layout_marginEnd="16dp"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            android:text='@{app.departureBusStopName}' />

        <TextView
            android:id="@+id/arrivalBusStopNameTextView"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            app:layout_constraintTop_toBottomOf="@id/departureBusStopNameTextView"
            app:layout_constraintStart_toStartOf="@id/departureBusStopNameTextView"
            app:layout_constraintEnd_toEndOf="@id/departureBusStopNameTextView"
            android:text='@{app.arrivalBusStopName}' />

        <TextView
            android:id="@+id/routeNamesTextView"
            android:layout_width="0dp"
            android:layout_height="0dp"
            android:layout_marginTop="16dp"
            android:layout_marginBottom="16dp"
            app:layout_constraintTop_toBottomOf="@id/arrivalBusStopNameTextView"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintStart_toStartOf="@id/arrivalBusStopNameTextView"
            app:layout_constraintEnd_toEndOf="@id/arrivalBusStopNameTextView"
            android:text='@{app.routeNames}' />

    </androidx.constraintlayout.widget.ConstraintLayout>

</layout>
~~~

`<data>`タグの中の`<variable>`タグで、このレイアウトで扱うデータを定義します。今回は`App`のプロパティを使用して画面を表示するので、`App`にします。

で、`android:text`属性の値の`@{...}`の部分がバインディング先の指定です。`app.departureBusStopName`は`LiveData`型だけど文字列型にしなくていいの？　とお考えになった方がいらっしゃるかもしれませんけど、データ・バインディングは`LiveData`対応ですのでこれで大丈夫なんです。

ただ、良いことばかり続くわけではないのが世の常です。`routeNamesTextView`の`android:text`プロパティを見てください。`map()`や`joinToString()`を使わずに、まだ定義してない`App`の`routeNames`プロパティをバインディングしています。なんでこんなことをしているかというと、データ・バインディングの`@{...}`の中ってKotlinのコードを書けないんためです。詳しい言語仕様は[レイアウトとバインディング式](https://developer.android.com/topic/libraries/data-binding/expressions?hl=ja)に書いてあるので見ていただきたいのですけど、たとえばKotlinの`if`は式なので、Kotlinのプログラマーは`val x = if (condition) "OK" else "BAD"`のように書くのに慣れていますけど、データ・バインディングのときは昔懐かしい三項演算子（`condition ? "OK" : "BAD"`）で書かないとダメだったりします。

やってられないので、あっさり諦めて`App`にプロパティを追加しました。`App`に追加というとやっちゃいけないことのように感じられますけど、本章の最後までやればViewModelへの追加となります。ViewModelはViewのためのものなのですから、この程度は良いんじゃないかなぁと。ViewModelがViewを呼び出すのはダメですけど、Viewのことを考えながらViewModelを作るのはアリなのですから。もちろん、こんなプロパティは無いほうがいいのですけど、でも、言語仕様がアレなんだもん。

~~~ kotlin
package com.tail_island.jetbus

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tail_island.jetbus.model.AppDatabase
import com.tail_island.jetbus.model.Route
import javax.inject.Inject

class App: Application() {
    ...

    val routeNames = Transformations.map(routes) { routesValue ->
        routesValue.map { it.name }.joinToString("\n")
    }

    ...
}
~~~

最後。`BusApproachesFragment`を修正します。

~~~ kotlin
package com.tail_island.jetbus

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tail_island.jetbus.databinding.FragmentBusApproachesBinding
import kotlin.concurrent.thread

class BusApproachesFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentBusApproachesBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner

            app = (requireActivity().application as App)

            thread {
                Thread.sleep(5000)

                (requireActivity().application as App).departureBusStopName.postValue("日本ユニシス本社前")
                Log.d("BusApproachesFragment", "MutableLiveData.postValue()")

                Thread.sleep(5000)

                (requireActivity().application as App).departureBusStopName.postValue("深川第八中学校前")
            }
        }.root
    }
}
~~~

不要になった`observe()`メソッドの呼び出しをまるっと削除して、データ・バインディングが`LiveData`を監視できるように`lifeCycleOwner`プロパティを設定して、あと、`<data>`タグで設定した`app`プロパティを設定しているだけ。`thread { ... }`の部分はテスト用のコードなので最後にはなくなりますから、かなりシンプルなコードですよね？　こんな単純になっても、今までと同じ動作をしてくれるんですよ。

うん、LiveDataとデータ・バインディングは、コード量を激減させてくれて素晴らしいですな。

## Repository

やっとLiveDataが終わりましたから、あとはRepositoryを作るだけでViewModelを作れます。とはいっても、ただ普通に`class`を作るだけで、とある事情（後の章で説明するコルーチンを使わないと書きづらい）でWebサービス側はとりあえず作らないから、やたらと簡単なんですけどね。

~~~ kotlin
package com.tail_island.jetbus.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val database: AppDatabase) {
    fun getObservableBusStopsByDepartureBusStopName(departureBusStopName: String) = database.getBusStopDao().getObservablesByDepartureBusStopName(departureBusStopName)
    fun getObservableRoutesByDepartureBusStopNameAndArrivalBusStopName(departureBusStopName: String, arrivalBusStopName: String) = database.getRouteDao().getObservablesByDepartureBusStopNameAndArrivalBusStopName(departureBusStopName, arrivalBusStopName)
}
~~~

はい、これだけ。`AppDatabase`のメソッドをラップしているだけです。`@Singleton`アノテーションや`@Inject`アノテーションが付いている理由は、もう少し後で。

## ViewModel

ついにViewModelです。ここまで長かった……。ViewModelは、`ViewModel`を継承して作成します。

~~~ kotlin
package com.tail_island.jetbus.view_model

import android.util.Log
import androidx.lifecycle.*
import com.tail_island.jetbus.model.Repository
import com.tail_island.jetbus.model.Route

class BusApproachesViewModel(private val repository: Repository): ViewModel() {
    val departureBusStopName = MutableLiveData<String>()

    val arrivalBusStops = Transformations.switchMap(departureBusStopName) { arrivalBusStopNameValue ->
        repository.getObservableBusStopsByDepartureBusStopName(arrivalBusStopNameValue)
    }

    val arrivalBusStopName = Transformations.map(arrivalBusStops) { arrivalBusStopsValue ->
        if (arrivalBusStopsValue.isEmpty()) {
            return@map null
        }

        arrivalBusStopsValue[0.until(arrivalBusStopsValue.size).random()].name
    }

    val routes = MediatorLiveData<List<Route>>().apply {
        var source: LiveData<List<Route>>? = null

        fun update() {
            val departureBusStopNameValue = departureBusStopName.value ?: return
            val arrivalBusStopNameValue   = arrivalBusStopName.value   ?: return

            source?.let {
                removeSource(it)
            }

            source = repository.getObservableRoutesByDepartureBusStopNameAndArrivalBusStopName(departureBusStopNameValue, arrivalBusStopNameValue).also {
                addSource(it) { sourceValue ->
                    value = sourceValue
                }
            }
        }

        addSource(departureBusStopName) { update() }
        addSource(arrivalBusStopName)   { update() }
    }

    val routeNames = Transformations.map(routes) { routesValue ->
        routesValue.map { it.name }.joinToString("\n")
    }
}
~~~

……ここまでもったいぶっておいて何なのですけど、前述したように`ViewModel`を継承するようにして、これまでとりあえずAppに書いていたコードを移動させ、`AppDatabase`ではなく`Repository`のメソッドを呼び出すように修正しただけです。

## `ViewModel`をDaggerから取得する

では、ViewModelを使ってみましょう。ドキュメントの[ViewModelの概要](https://developer.android.com/topic/libraries/architecture/viewmodel?hl=ja)を見てみると、なるほど、`ViewModelProviders.of(this)[BusApproachesViewModel::class.java]`でインスタンスを取得できるのね……って、これではインスタンス生成が自動化されていて手が出せないので、`Repository`を引数にしてコンストラクタを呼び出すことできないじゃん！

同じ問題は`Activity`や`Fragment`で経験済みで、あのときはDaggerによるDependency Injectionで解決できました。でも、`ViewModel`からは`App`にアクセスできないので、同じ解決策は使えません……。

幸いなことに、Daggerは有名なプロダクトですから、いろいろな人が使い方を調べて解説を書いてくださっています。その一つが[ViewModelをDagger2でDIする](https://qiita.com/superman9387/items/bea94e4316c2ccf8fb68)です。ありがてぇ。というわけで、ここに書いてある通りにすればオッケー。ちなみに、難しすぎて私は中身を理解できてません。たぶん、`ViewModelPrividesr`をいい感じに騙しているんだと思う。

というわけで、黙々と作業します。解説と順序は違いますが、まずは`ViewModelKey`アノテーションと`AppViewModelProviderFactory`を作成します。新規にUtility.ktを作成して、以下の内容を入力します。

~~~ kotlin
package com.tail_island.jetbus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.MapKey
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

// ViewModel and Dagger.

@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)

class AppViewModelProvideFactory @Inject constructor(private val factories: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return factories.entries.find { modelClass.isAssignableFrom(it.key) }!!.value.get() as T
    }
}
~~~

`AppModule`はこんな感じ。

~~~ kotlin
package com.tail_island.jetbus

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.tail_island.jetbus.model.AppDatabase
import com.tail_island.jetbus.model.Repository
import com.tail_island.jetbus.model.WebService
import com.tail_island.jetbus.view_model.BusApproachesViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {
    ...

    @Provides
    @Singleton
    fun provideDatabase(context: Context) = Room.databaseBuilder(context, AppDatabase::class.java, "jetbus.db").build()

    @Provides
    @IntoMap
    @ViewModelKey(BusApproachesViewModel::class)
    fun provideBusApproachesViewModel(repository: Repository) = BusApproachesViewModel(repository) as ViewModel
}
~~~

`AppComponent`に`BusApproachesFragment`に依存性を注入するメソッドを定義します。

~~~ kotlin
package com.tail_island.jetbus

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(splashFragment: SplashFragment)
    fun inject(busApproachesFragment: BusApproachesFragment)
}
~~~

この章で`App`に追加したコードを全部削除して、で、`BusApproachesFragment`を修正します。

~~~ kotlin
package com.tail_island.jetbus

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.tail_island.jetbus.databinding.FragmentBusApproachesBinding
import com.tail_island.jetbus.view_model.BusApproachesViewModel
import javax.inject.Inject
import kotlin.concurrent.thread

class BusApproachesFragment: Fragment() {
    @Inject lateinit var viewModelProviderFactory: AppViewModelProvideFactory

    private val viewModel by viewModels<BusApproachesViewModel> { viewModelProviderFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (requireActivity().application as App).component.inject(this)

        return FragmentBusApproachesBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel      = this@BusApproachesFragment.viewModel

            thread {
                Thread.sleep(5000)

                viewModel.departureBusStopName.postValue("日本ユニシス本社前")
                Log.d("BusApproachesFragment", "MutableLiveData.postValue()")

                Thread.sleep(5000)

                viewModel.departureBusStopName.postValue("深川第八中学校前")
            }
        }.root
    }
}
~~~

解説とは`ViewModel`の作成方法が違うじゃんと思われたと思いますが、これ、Android JetpackのKotlin向け便利機能の一つなんです。

この書き方にしておくと、たとえば認証情報のような`Fragment`よりも生存期間が長い（別の`Fragment`に遷移したらまたIDとパスワード入力するなんてやってられないですよね？）ViewModelを使う場合に、`private val authorizationViewModel by activityViewModel<AuthorizationViewModel> { viewModelProviderFactory }`とするだけで、`ViewModel`の生存期間が`Activity`と同じになるのでとても便利なのですよ。

はい、これで完成！　正しく動くか試してみましょう。

動画

うん、動いた……のですけど、確認のために`AppModule`を開いてソース・コードを眺めていたら、なんかちょっと気持ち悪い。`fun provideBusApproachesViewModel(repository: Repository) = BusApproachesViewModel(repository) as ViewModel`の`repository`は、どうやって取得したのでしょうか？　だって、`Repository`を`@Provides`するメソッドは定義していないんですよ？

その答えは「`@Inject`アノテーションが付いたコンストラクタを持つクラスは、自動で`@Provides`なメソッドを作成してくれるから」です。先程`Repository`を作成したときに、`@Singleton`アノテーションと`@Inject`アノテーションを追加しましたよね。この`@Inject`アノテーションが役に立ってくれたんです。

では`@Singleton`アノテーションは何なのかというと、`@Singleton`をDaggerで生成したい型に付加しておくと、`@Provides`メソッドを生成するときに`@Singleton`が自動で付くようになるんです。`Repository`は何個もいらないですもんね。というわけで、`AppModule`の`@Singleton`は生成対象の型に移動……させるという手は、`provideContext()`や`provideConsumerKey()`では使えません。書き方が統一できないとミスが発生しそうで怖いのですけど、しょうがないので、念の為に両方に書くという方式にしましょう。`AppDatabase`と`WebService`にも`@Singleton`を付加しておきます。

というわけで、コンストラクタに`@Inject`を書くだけで依存性を注入しまくれるんです。注入した依存性を活用できるのは`ViewModel`経由で呼び出す場合だけですけど、MVVMアーキテクチャを採用してアプリを組むのですから問題とはなりません。ほら、前章からの宿題だったDaggerをどうやって使うかが決まったでしょ？　これ以降は、依存性を注入したいならコンストラクタに`@Inject`を書くだけでよくなったんですよ。

まぁ、`Activity`や`Fragment`には、依存性を注入する処理を手で書かなければならないんだけどな。

# [コルーチン](https://github.com/tail-island/jetbus/tree/coroutines)

もろもろ片付いたのでよーしパパ残りのViewModelも作っちゃうぞーと考えたのですけど、最初の画面の`SplashFragment`向けのViewModelでいきなり詰まってしまいました。RoomやRetrofit2はメイン・スレッドからは呼び出せないのですけど、スレッドどうしましょ？

ViewModelの中で`thread`で別スレッドを生成する……のは前の章で書いておいてアレなのですけど、ダメ、絶対。だって、生成されたスレッドは終了するまで生き残ってしまうんですから。ViewModelを作成してその中でスレッドを生成して、画面遷移して`Fragment`が終了するとかでViewModelを終了する。この場合でも、スレッドは処理が終わるまで動き続けます。で、もう一度同じ`Fragment`が表示されたりしてViewModelが生成されると、またスレッドが生成されちゃう。負荷が大きいことに加えて、処理が2重に動いてしまうんですよ。実は前の章までで作成したアプリにはこの問題に起因するバグがあって、`SplashFragment`でスレッドを生成してデータベースを更新しているときにスマートフォンを回転させたりすると、`Activity`が再生成されて`Fragment`が再生成されてデータベースを更新するスレッドがもう一つ動いて、データの不整合ができてアプリが落ちちゃうんですよ。これじゃあ困る。

だから、スレッドの生存期間をViewModelと同じになるようにしなければなりません。そんなときに便利なのが、途中で処理を中断したり再開できたり、さらには中断したまま途中でやめちゃったりもできる、コルーチンなんです。

## 途中で処理を中断、再開？

途中で処理を中断したり再開したりするというのがどういうことなのか、具体的なコードでご説明します。

~~~ kotlin
fun useCoroutine() {
    runBlocking {  // とりあえず、runBlockingは無視してください……
        for (i in 3.downTo(1)) {
            launch {
                delay(i.toLong() * 1000)
                Log.d("xxx", "${i}")
            }
        }
    }
}
~~~

このコードを実行すると、「1」のあとに「2」、そのあとに「3」が表示されます。`for`文は`3.downTo(1)`となっているので`i`の値は3、2、1の順なのですけど、`launch`の中身は非同期に実行され、`delay()`は指定した時間（ミリ秒）待つので、1,000ミリ秒待って「1」、2,000ミリ秒待って「2」、3,000ミリ秒まって「3」というループとは逆の順に出力されるというわけですな。

……ってそれ、`thread`と`Thread.sleep()`でも同じことができるのでは？　たとえば、こんなコードで。

~~~ kotlin
fun useThread() {
    for (i in 3.downTo(1)) {
        thread {
            Thread.sleep(i.toLong() * 1000)
            Log.d("xxx", "${i}")
        }
    }
}
~~~

はい、その通り。`thread`を使ったこのコードでも、確かに出力は同じになります。でも、`useCoroutine()`は実はとてもすごくて、*新しいスレッドを使わずに、すべてメイン・スレッドで実行*しているんです。確認してみましょう。

~~~ kotlin
fun useCoroutine() {
    Log.d("xxx", "Main thread id: ${Thread.currentThread().id}")

    runBlocking {
        for (i in 3.downTo(1)) {
            launch {
                delay(i.toLong() * 1000)
                Log.d("xxx", "${i}: ${Thread.currentThread().id}")
            }
        }
    }
}

fun useThread() {
    Log.d("xxx", "Main thread id: ${Thread.currentThread().id}")

    for (i in 3.downTo(1)) {
        thread {
            Thread.sleep(i.toLong() * 1000)
            Log.d("xxx", "${i}: ${Thread.currentThread().id}")
        }
    }
}
~~~

このコードを実行すると、`useCoroutine()`の方ではすべて同じスレッドIDが、`useThread()`の方ではすべて異なるスレッドIDが出力されます。ほら、`useCoroutine()`の方は、すべてメイン・スレッドで実行されていてスゴイでしょ？

でも、一つのスレッドでは一つのことしかできないはずで、だから、「待つ」のと「出力」の両方は実行できないはず。でもできちゃっているのはなぜかといえば、実は`delay()`は「待つ」のではなく「中断」だからなんです。中断している間は他のコルーチンを実行できるので、だから並列処理に見えたというわけ。たとえば`delay(1000)`なら、中断して1,000ミリ秒たったら再開するという意味になるんですよ（とはいえ、メイン・スレッドで動く他の処理がいつまでも終わらなかったりするような場合は、1,000ミリ秒たっても再開されなかったりしますけど）。

## 中断して、実行するスレッドを変えて、再開しちゃえ！

コルーチンのすごいのは、それだけじゃありません。実行するスレッドを変更することができるんです。

~~~ kotlin
Log.d("xxx", "Main thread id: ${Thread.currentThread().id}")

runBlocking {
    launch {
        Log.d("xxx", "Hello: ${Thread.currentThread().id}")

        withContext(Dispatchers.IO) {
            Log.d("xxx", "Coroutines: ${Thread.currentThread().id}")
        }

        Log.d("xxx", "World: ${Thread.currentThread().id}")
    }
}
~~~

上のコードを実行すると、`withContext()`の中だけスレッドIDが変わっていることが分かります。しかも、実行順序は「Hello」「Coroutines」「World」の順序を保っています。まぁ、中断して再開できるのですから、中断してゴニョゴニョして別のスレッドで再開できても不思議ではありませんよね？

なお、上のコードの`Dispatchers.IO`はファイル操作やデータベース・アクセス、Webサービス呼び出し等向けで、他には、`Dispatchers.Default`（バック・グラウンドでの計算処理など向け）、`Dispatchers.Main`（メイン・スレッド）があります。というわけで、`withContext(Dispatchers.IO) {}`すれば、データベース・アクセスもWebサービス呼び出しもやりたい放題ですよ。

## 中断して、そのままやめちゃえ！

`ViewModel`の中で`viewModelScope.launch`した場合は、もっと面白いです。

~~~ kotlin
package com.tail_island.jetbus.view_model

import android.util.Log
import androidx.lifecycle.*
import com.tail_island.jetbus.model.Repository
import com.tail_island.jetbus.model.Route
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BusApproachesViewModel(private val repository: Repository): ViewModel() {
    ...

    fun foo() {
        viewModelScope.launch {
            while (true) {
                delay(1000)

                Log.d("xxx", "viewModelScope")
            }
        }
    }
}
~~~

`runBlocking`と`launch`ではなく、`viewModelScope.launch`しています。中身は無限ループなので、いつまでも実行されるはず。

~~~ kotlin
package com.tail_island.jetbus

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.tail_island.jetbus.databinding.FragmentBusApproachesBinding
import com.tail_island.jetbus.view_model.BusApproachesViewModel
import javax.inject.Inject
import kotlin.concurrent.thread

class BusApproachesFragment: Fragment() {
    @Inject lateinit var viewModelProviderFactory: AppViewModelProvideFactory

    private val viewModel by viewModels<BusApproachesViewModel> { viewModelProviderFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (requireActivity().application as App).component.inject(this)

        viewModel.foo()

        return FragmentBusApproachesBinding.inflate(inflater, container, false).apply {
            ...
        }.root
    }
}
~~~

こんな感じで、先程の無限ループする処理を呼び出しています。`delay(1000)`の間にメイン・スレッドは別の処理をできるのでユーザー・インターフェースが止まらず、それでいてもちろん、logcatにログが出力されます。で、それだけではなくて、画面が遷移すると無限ループなのに処理が止まるんですよ！

中断して、再開しなければいい（あと、再開に必要な情報を破棄する）だけですもんね。うん、できるの分かってた。`viewModelScope`は`ViewModel`と同じライフサイクルを持っているので、画面が遷移して`ViewModel`が破棄されると処理が止まるというわけ。

あと、`viewModelScope`の他に、`Activity`や`Fragment`で使用できる`lifecycleScope`や、アプリと同じライフサイクルを持つ`GlobalScope`なんてものあります。でも、できるだけ`GlobalScope`は使わないでくださいね。

## 関数に抽出しちゃえ！

さて、ここまでのコードに出てきた`launch {}`はコルーチンを生成するのですけど、その中に大きな処理を書くと可読性が下がってしまいますから、小さな関数に分割したい。でも、以下のコードはコンパイル・エラーになってしまうんです。

~~~ kotlin
fun bar() {
    delay(1000)

    Log.d("xxx", "viewModelScope")
}

fun foo() {
    viewModelScope.launch {
        while (true) {
            bar()
        }
    }
}
~~~

というのも、`delay()`はコルーチンの中だからできる特別な処理なわけで、だからコルーチンではない場所から呼び出されても困っちゃう。だから、コルーチンの中から呼び出す特別な関数とするために、`suspend`を付加しなければならないんです。

~~~ kotlin
suspend fun bar() {  // suspendを付加
    delay(1000)

    Log.d("xxx", "viewModelScope")
}

fun foo() {
    viewModelScope.launch {
        while (true) {
            bar()
        }
    }
}
~~~

はい、これでコンパイルに通ります。なお、`suspend`な関数はコルーチン向けの特別な関数ですから、コルーチンの外では呼び出せません。`launch {}`の中や`suspend`な関数の中から呼び出してあげてください。

## SplashFragmentのデータベース・アクセスとWebサービス呼び出しをコルーチンにする

というわけでコルーチンの説明が終了したので（他にもいろいろな便利機能があるので、ぜひ[公式ガイド](https://kotlinlang.org/docs/reference/coroutines/coroutines-guide.html)を読んでみてください）、アプリの実装を勧めましょう。

### `Repository`

まずは、`Repository`にデータをクリアする`suspend`なメソッドと[公共交通オープンデータセンター](https://www.odpt.org/)のWebサービスを呼び出してデータベースにキャッシュする`suspend`なメソッドを作ります。

~~~ kotlin
package com.tail_island.jetbus.model

import android.util.Log
import androidx.room.withTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val database: AppDatabase, private val webService: WebService, @param:Named("consumerKey") private val consumerKey: String) {
    suspend fun clearDatabase() = withContext(Dispatchers.IO) {
        try {
            database.withTransaction {
                database.getTimeTableDetailDao().clear()
                database.getTimeTableDao().clear()
                database.getRouteBusStopPoleDao().clear()
                database.getRouteDao().clear()
                database.getBusStopPoleDao().clear()
                database.getBusStopDao().clear()
            }

            Unit

        } catch (e: IOException) {
            Log.e("Repository", "${e.message}")
            null
        }
    }

    private fun <T> getWebServiceResultBody(callWebService: () -> Call<T>): T? {
        val response = callWebService().execute()

        if (!response.isSuccessful) {
            Log.e("Repository", "HTTP Error: ${response.code()}")
            return null
        }

        return response.body()
    }

    suspend fun syncDatabase() = withContext(Dispatchers.IO) {
        try {
            if (database.getBusStopDao().getCount() > 0) {
                return@withContext Unit
            }

            val busStopPoleJsonArray = getWebServiceResultBody { webService.busstopPole(consumerKey)     } ?: return@withContext null
            val routeJsonArray =       getWebServiceResultBody { webService.busroutePattern(consumerKey) } ?: return@withContext null

            database.withTransaction {
                for (busStopPoleJsonObject in busStopPoleJsonArray.map { it.asJsonObject }.filter { it.get("odpt:operator").asString == "odpt.Operator:Toei" }) {
                    val busStop = database.getBusStopDao().getByName(busStopPoleJsonObject.get("dc:title").asString) ?: run {
                        BusStop(
                            busStopPoleJsonObject.get("dc:title").asString,
                            busStopPoleJsonObject.get("odpt:kana")?.asString
                        ).also {
                            database.getBusStopDao().add(it)
                        }
                    }

                    BusStopPole(
                        busStopPoleJsonObject.get("owl:sameAs").asString,
                        busStop.name
                    ).also {
                        database.getBusStopPoleDao().add(it)
                    }
                }

                for (routeJsonObject in routeJsonArray.map { it.asJsonObject }.filter { it.get("odpt:operator").asString == "odpt.Operator:Toei" }) {
                    val route = Route(
                        routeJsonObject.get("owl:sameAs").asString,
                        routeJsonObject.get("dc:title").asString
                    ).also {
                        database.getRouteDao().add(it)
                    }

                    for (routeBusStopPoleJsonObject in routeJsonObject.get("odpt:busstopPoleOrder").asJsonArray.map { it.asJsonObject }) {
                        RouteBusStopPole(
                            route.id,
                            routeBusStopPoleJsonObject.get("odpt:index").asInt,
                            routeBusStopPoleJsonObject.get("odpt:busstopPole").asString
                        ).also {
                            it.id = database.getRouteBusStopPoleDao().add(it)
                        }
                    }
                }
            }

            Unit

        } catch (e: IOException) {
            Log.e("Repository", "${e.message}")
            null
        }
    }

    ...
}
~~~

中身は、前に`SplashFragment`の中に書いたコードのほぼコピー＆ペーストです。変更点は、`clearDatabase()`と`syncDatabase()`に分割したのと、`withContext(Dispatchers.IO)`で囲んだのと、成功（Unit）か失敗（null）を返すようにしたこと（前にどこかで書きましたけど、ラムダ式は最後に実行する行の値が返り値になります）と、毎回データを取得するのは時間がかかって嫌なので`if (database.getBusStopDao().getCount() > 0)`なら処理をしないというチェック・ロジックを付加した（`getCount()`は`@Query("SELECT COUNT(*) FROM BusStop")`アノテーションで作成しました）のと、そのチェック・ロジックが正常に動作するように`database.withTransaction {}`で囲んだことくらい。Roomのところで説明し忘れたのですけど、`database.withTransaction {}`で囲むと処理がアトミックになる（完全にやるか、全くやらないかのどちらかになる）ので便利ですよ。

### `SplashViewModel`

次は`ViewModel`です。SplashViewModel.ktを追加して、以下のコードを書きました。

~~~ kotlin
package com.tail_island.jetbus.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tail_island.jetbus.model.Repository
import kotlinx.coroutines.launch

class SplashViewModel(private val repository: Repository): ViewModel() {
    val isSyncDatabaseFinished = MutableLiveData<Boolean>()

    init {
        viewModelScope.launch {
            repository.syncDatabase() ?: run { isSyncDatabaseFinished.value = false; return@launch }

            isSyncDatabaseFinished.value = true
        }
    }
}
~~~

`init {}`には、インスタンスが生成されたときに実行する処理を記述します。本章の最初に書いたように、Webサービスを呼び出してデータベースにキャッシュするコルーチンは、`ViewModel`と同じライフサイクルを持っていて欲しい。だから生成と同時に`viewModelScope.launch {}`しているわけですな。`viewModelScope`で`launch`しているのでViewModelが破棄されればこのコルーチンは終了しますから、終了もバッチリ。アプリがバックグラウンドになればコルーチンが止まるので、他のアプリに迷惑をかけないですし。

`isSyncDatabaseFinished`プロパティは、`syncDatabase()`が成功したか失敗したかを表現する目的で追加しました。ViewModelは`isSyncDatabaseFinished`を`observe()`して、正常に終了したら次の画面に遷移、そうでなければアプリケーションを終了するとかすればよいわけ。

### `SplashFragment`

というわけで、`SplashFragment`には`isSyncDatabaseFinished`を`observe()`する処理を追加して、その代わりにWebサービスを呼び出したりデータベースにアクセスしたりしていた処理をまるごと削除しました。こんな感じ。

~~~ kotlin
package com.tail_island.jetbus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.tail_island.jetbus.databinding.FragmentSplashBinding
import com.tail_island.jetbus.view_model.SplashViewModel
import javax.inject.Inject

class SplashFragment: Fragment() {
    @Inject lateinit var viewModelProviderFactory: AppViewModelProvideFactory

    private val viewModel by viewModels<SplashViewModel> { viewModelProviderFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (requireActivity().application as App).component.inject(this)

        viewModel.isSyncDatabaseFinished.observe(viewLifecycleOwner, Observer {
            if (!it) {
                requireActivity().finish()
                return@Observer
            }

            findNavController().navigate(SplashFragmentDirections.splashFragmentToBookmarksFragment())
        })

        return FragmentSplashBinding.inflate(inflater, container, false).root
    }
}
~~~

うん、スッキリしました（`isSyncDatabaseFinished`が`Unit?`ならエルビス演算子が使えてもっとスッキリするのですけど、プロパティ名を「is」で始めたので`Boolean`にせざるを得なかった……）。

というわけで、これで自動で画面遷移するようになったのでもうボタンは不要になりましたから、`fragment_splash.xml`を以下に変更します。

~~~ kotlin
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    tools:context=".SplashFragment">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <ImageView
            android:id="@+id/icon"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginBottom="8dp"
            app:layout_constraintStart_toStartOf="@id/nowDownloadingTextView"
            app:layout_constraintEnd_toEndOf="@id/nowDownloadingTextView"
            app:layout_constraintBottom_toTopOf="@id/nowDownloadingTextView"
            android:contentDescription="@string/app_name"
            android:src="@mipmap/ic_launcher_round" />

        <TextView
            android:id="@+id/nowDownloadingTextView"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintBottom_toBottomOf="parent"
            android:text="バス停と路線の情報を取得しています……" />

        <ProgressBar
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            app:layout_constraintStart_toStartOf="@id/nowDownloadingTextView"
            app:layout_constraintEnd_toEndOf="@id/nowDownloadingTextView"
            app:layout_constraintTop_toBottomOf="@id/nowDownloadingTextView" />

    </androidx.constraintlayout.widget.ConstraintLayout>

</layout>
~~~

アイコンと、何をしているのかの説明と、あとは処理に時間がかかるので`<ProgressBar>`という名前のぐるぐるアイコンですな。これで`SpashFragment`は完成です。

### `MainActivity`

でも、このままだと一回`syncDatabase()`に成功しちゃうと次からは実行しないので、プログラムが正しく動くのかよく分かりません……。なので、`menu_navigation.xml`に追加したきりすっかり忘れていた`<item android:id="@+id/clearDatabase" android:title="バス停と路線のデータを再取得" />`のリスナーを作成しましょう。

~~~ kotlin
package com.tail_island.jetbus

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.tail_island.jetbus.databinding.ActivityMainBinding
import com.tail_island.jetbus.view_model.MainViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity: AppCompatActivity() {
    @Inject lateinit var viewModelProviderFactory: AppViewModelProvideFactory

    private val viewModel by viewModels<MainViewModel> { viewModelProviderFactory }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as App).component.inject(this)

        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
            navigationView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.clearDatabase -> run {
                        lifecycleScope.launch {
                            viewModel.clearDatabase()
                            findNavController(R.id.navHostFragment).navigate(R.id.splashFragment)
                        }

                        true
                    }
                    else -> false

                }.also {
                    if (!it) {
                        return@also
                    }

                    drawerLayout.closeDrawer(GravityCompat.START)
                }
            }

        }.also {
            findNavController(R.id.navHostFragment).apply {
                NavigationUI.setupWithNavController(it.toolbar, this, AppBarConfiguration(setOf(R.id.bookmarksFragment), it.drawerLayout))

                addOnDestinationChangedListener { _, destination, _ ->
                    it.appBarLayout.visibility = if (destination.id == R.id.splashFragment) View.GONE else View.VISIBLE
                }
            }
        }
    }

    ...
}
~~~

`SplashViewModel`のときのように`clearDatabase()`が完了したかどうかを表現するプロパティを追加する方式だとコード量が増えてしまうので、今回は`lifecycleScope.launch {}`することにしました。

`MainViewModel`の中身は、以下のようにとても単純です。

~~~ kotlin
package com.tail_island.jetbus.view_model

import androidx.lifecycle.ViewModel
import com.tail_island.jetbus.model.Repository

class MainViewModel(private val repository: Repository): ViewModel() {
    suspend fun clearDatabase() = repository.clearDatabase()
}
~~~

`Repository`のメソッドを呼び出し直しているだけ。あとは、前の章で紹介した手順で`ViewModel`をDaggerで注入可能にすれば、作業終了です。ぜひ実際に動かしてみてください。ほらこのアプリ、端末を回転させても正常に動作するんです！

……そんなの当たり前だと思うかもしれませんけど、Androidアプリ開発ではこの程度でも大変だったんだよ。

# [RecyclerView](https://github.com/tail-island/jetbus/tree/recycler-view)

と、ここまでいろいろ作ってきましたけど、なのにいまだに画面がスカスカで悲しい……。画面を作りましょう。使う道具は、これを覚えるだけで閲覧系のアプリなら大体どうにかなっちゃうという噂の`RecyclerView`です。

## リサイクル？

`RecyclerView`は、スクロール可能なリストを作成するGUIウィジェットです。特徴は大規模なデータ・セットに対応可能なこと（大は小を兼ねるので、小さなデータ・セットで使っても特に問題はありませんけど）。

で、この「大規模」という点が、`RecyclerView`という名前つながります。Androidの画面は`View`（本稿でも、文字を表示するための`TextView`とかを使いましたよね？）で構成されるので、リストの各行も`View`で構成されます。画面を上にスクロールすると、上の行が画面の外側に消えて下に新しい行が表示されるわけですけど、その画面の外側に消えた行の`View`をどうしましょうか？　放っておくとメモリに負荷がかかるし、ガベージ・コレクションの際にはCPUに負荷がかかっちゃう。だから、下から出てくる新しい行の`View`としてリサイクルしたい。これを自動でやってくれるのが`RecyclerView`というわけ。

## `ViewHolder`と`DiffCallback`とAdapterを作る

リストを表示する場合、リストそのものを管理する人とリストの要素を管理する人を分けた方が楽になります。`List<MyClass>`って分割するのと一緒。`RecyclerView`の場合、リストの要素は`ViewHolder`で管理します。で、`ViewHolder`と`RecyclerView`を繋げるのが`ListAdapter`。まずはこの`ViewHolder`の説明から。

`RecyclerView`は`View`をリサイクルするので、これまでは会社の前のバス停を表示していた`View`に対して、これからは私のアパートの近くのバス停を表示するように指示できなければなりません。これは、前に説明したデータ・バインディングで実現すれば簡単でしょう。レイアウトのXMLの`<data>`タグの中身を変えちゃえばいいんですからね。

というわけで、たとえばバス停をリスト表示する`ViewHolder`向けのレイアウトXMLは以下のようになります。名前はlist_item_bus_stop.xmlにしましょう。

~~~ xml
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <data>
        <variable name="item" type="com.tail_island.jetbus.model.BusStop" />
    </data>

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <com.google.android.material.button.MaterialButton
            android:id="@+id/busStopButton"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            android:gravity="start|center_vertical"
            style="@style/Widget.MaterialComponents.Button.TextButton"
            android:text='@{item.name}' />

    </androidx.constraintlayout.widget.ConstraintLayout>

</layout>
~~~

`<data>`タグの中の`item`の値を変更することで、`ViewHolder`のリサイクルを実現するわけですな。`<com.google.android.material.button.MaterialButton>`はAndroidの標準のデザイン・ガイドラインである[マテリアル・デザイン](https://material.io/)のボタンを実現するボタンで、見た目がかっこいい上に`style`を指定するだけで見た目を大きく変えられます。今回は、背景がなくて文字だけの`@style/Widget.MaterialComponents.Button.TextButton`にしました。

このレイアウトを使用する`ViewHolder`はこんな感じ。

~~~ kotlin
class ViewHolder(private val binding: ListItemBusStopBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: BusStop) {
        binding.item = item
        binding.executePendingBindings()

        binding.busStopButton.setOnClickListener {
            // ここに、バス停がタップされたときの処理を入れる
        }
    }
}
~~~

`bind()`メソッドは、`ViewHolder`にデータを設定するためのメソッドです（このメソッドを呼び出す部分は`ListAdapter`で作るのでちょっと待って）。`<data>`タグの`item`を変更して、`executePendingBindings()`でデータ・バインディングを強制的に実行させて、あと、リスナーを再設定しています。

でもちょっと待って。リストが変更された場合のことを考えてみましょう。たとえば最後に1行追加された場合は、残りの行は何もしなくてもよいはず。でも、今回のようにデータベースからデータを取得する場合は、再取得すると異なるインスタンスになってしまいます。以前表示していた`BusStop`のインスタンスと今回表示する`BusStop`のインスタンスは、同じバス停を表している場合であっても異るというわけ。異なるインスタンスなのだからもう一度データ・バインディングをやり直しましょうというのは、リソースの無駄遣いなのでやりたくない。あと、`RecyclerView.ViewHolder`のAPIリファレンスを眺めてみると、リストの中のどの位置なのかを表現する`getPosition()`というメソッドがありました。他にも、`getOldPosition()`という位置を移動するアニメーションのためのメソッドも。ということは、`ViewHolder`はリストの中を上下に移動することが可能なはず。それはそうですよね。最初に一行挿入された場合に、全部を書き直すのは無駄ですもん。

というわけで、データの変更が無いから再バインディングは不要と判断したり、異なるインスタンスだけど同じデータを表現しているので上下への移動で対応しようと判断したりするための機能が必要です。これが`DiffCallback`です。コードは以下に示すとおりで、`DiffUtil.ItemCallback`を継承して作成します。

~~~ kotlin
class DiffCallback: DiffUtil.ItemCallback<BusStop>() {
    override fun areItemsTheSame(oldItem: BusStop, newItem: BusStop) = oldItem.name == newItem.name
    override fun areContentsTheSame(oldItem: BusStop, newItem: BusStop) = oldItem == newItem
}
~~~

`areItemsTheSame()`が同じ要素かを調べるメソッド、`areContentsTheSame()`メソッドが中身が変わっていないかを調べるメソッドです。我々はリレーショナル・データベースを使用していて、リレーショナル・データベースのエンティティは主キーを持っているので、`areItemsTheSame()`は主キーを比較するだけです。あと、Roomのエンティティは属性すべての値が同じかを調べる`==`演算子を定義してくれているので、`areContentsTheSame()`はただ単に`oldItem`と`newItem`を`==`で結ぶだけでオッケー。

と、こんな感じで`ViewHolder`と`DiffCallback`ができましたので、あとはこれらと`RecyclerView`をつなげてくれるAdapterを作るだけ。コードを以下に示します。

~~~ kotlin
package com.tail_island.jetbus.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tail_island.jetbus.databinding.ListItemBusStopBinding
import com.tail_island.jetbus.model.BusStop

class BusStopAdapter: ListAdapter<BusStop, BusStopAdapter.ViewHolder>(DiffCallback()) {
    lateinit var onBusStopClick: (busStop: BusStop) -> Unit

    inner class ViewHolder(private val binding: ListItemBusStopBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BusStop) {
            binding.item = item
            binding.executePendingBindings()

            binding.busStopButton.setOnClickListener {
                onBusStopClick(item)
            }
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<BusStop>() {
        override fun areItemsTheSame(oldItem: BusStop, newItem: BusStop) = oldItem.name == newItem.name
        override fun areContentsTheSame(oldItem: BusStop, newItem: BusStop) = oldItem == newItem
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder = ViewHolder(ListItemBusStopBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false))
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(getItem(position))
}
~~~

先程作成した`ViewHolder`と`DiffCallback`は、管理を簡単にするためにAdapterの中に入れました。Kotlinでは、単純に`class`の内側に`class`を作成しただけだと、外側の`class`の属性やメソッドを参照することはできません。で、`ViewHolder`では外側のクラスの`onBusStopClick`を使用したかったので、`inner class`にしました。

あとは、`ViewHolder`を作成する`onCreateViewHolder()`メソッドと、データ・バインディングのときに呼ばれる`onBindViewHolder()`メソッドを定義するだけです。GitHubのコードを見ていただければ分かるのですけど、Adapterはすべてほぼ同じコードです。コピー＆ペーストして少しだけ置換すれば完成しちゃうので、一度上のコードを覚えてしまえばとても簡単に作れますよ。

## RecyclerViewを組み込む

上で作成した`BusStopAdapter`を使う`RecyclerView`を、`DepartureBusStopFragment`に組み込みましょう。まずは、レイアウトのXMLです。

~~~ XML
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    tools:context=".DepartureBusStopFragment">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/recyclerView"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:padding="16dp"
            android:clipToPadding="false"
            app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager" />

    </androidx.constraintlayout.widget.ConstraintLayout>

</layout>
~~~

`android:clipToPadding`というのは、パディング範囲を超えた部分を消すかどうかです。なんでこんな指定をしているかというと、これが無いととても不自然なユーザー・インターフェースになってしまうから。マテリアル・デザインではスマートフォンの場合は端から16dpの隙間を空けることになっています。だから`android:padding="16dp"`で隙間を作成している。でも、上下にスクロールした時にこの16dpの隙間部分を消して真っ白にしてしまうと、表示している内容がどこに消えていったのか分からなくてとても不自然なユーザー・インターフェースになってしまうんですよ。だから`android:clipToPadding`を敢えて`false`にして消さないようにして、上方向はApp barの下に、下方向は画面の外に消えていったように見せているわけ。

あと、`app:layoutManager`というのは、`RecyclerView`の要素をどのように表示するかのレイアウトを決めるクラスです。`LinearLayoutManager`を指定すると、上から下にリスト状に表示されるようになります。

ここまできたら、BusStopDaoに`getObservables()`メソッドを、Repositoryに`getObservableBusStops()`メソッドを追加して、`DepartureBusStopViewModel`を作成して`departureBusStops`プロパティを追加してDaggerで挿入できるようにして、で、`DepartureBusStopFragment`を修正するだけ。コードはこんな感じ。

~~~ kotlin
package com.tail_island.jetbus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.tail_island.jetbus.adapter.BusStopAdapter
import com.tail_island.jetbus.databinding.FragmentDepartureBusStopBinding
import com.tail_island.jetbus.view_model.DepartureBusStopViewModel
import javax.inject.Inject

class DepartureBusStopFragment: Fragment() {
    @Inject lateinit var viewModelProviderFactory: AppViewModelProvideFactory

    private val viewModel by viewModels<DepartureBusStopViewModel> { viewModelProviderFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (requireActivity().application as App).component.inject(this)

        return FragmentDepartureBusStopBinding.inflate(inflater, container, false).apply {
            recyclerView.adapter = BusStopAdapter().apply {
                viewModel.busStops.observe(viewLifecycleOwner, Observer {
                    submitList(it)
                })

                onBusStopClick = {
                    findNavController().navigate(DepartureBusStopFragmentDirections.departureBusStopFragmentToArrivalBusStopFragment(it.name))
                }
            }
        }.root
    }
}
~~~

レイアウトのXMLに`<data>`がなかったのは、上のように`BusStopAdapter.submitList()`でコード上でやるため。データが変更になったら`submitList()`し直します（今回はデータが変更になることはないけど、変更があり得る場合と無い場合で記述を変えるのと保守性が落ちるので、変更がある場合向けのコードで統一します）。あとは、バス停がタップされた場合のリスナーに画面遷移をするコードを書いただけ。

## さらに、索引用の`RecyclerView`を作る

うん、これで完成……じゃないんですよ、今回のアプリでは。というのも、バス停の件数があまりに膨大なんです。だから、下の方のバス停を選ぶにはもーひたすらにスクロールしなければならなくて、とても実用にはなりません。実用に耐えられるよう、索引を作りましょう。もう一つ`RecyclerView`を作って重ねるだけの簡単な作業です。

list_item_index.xmlを追加します。

~~~ xml
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <data>
        <variable name="item" type="char" />
    </data>

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content">

        <com.google.android.material.button.MaterialButton
            android:id="@+id/indexButton"
            android:layout_width="0dp"
            android:layout_height="0dp"
            app:layout_constraintTop_toTopOf="@id/indexTextView"
            app:layout_constraintBottom_toBottomOf="@id/indexTextView"
            app:layout_constraintStart_toStartOf="@id/indexTextView"
            app:layout_constraintEnd_toEndOf="@id/indexTextView"
            style="@style/Widget.MaterialComponents.Button.TextButton"
            android:insetLeft="0dp"
            android:insetTop="0dp"
            android:insetRight="0dp"
            android:insetBottom="0dp" />

        <TextView
            android:id="@+id/indexTextView"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            android:text='@{String.format("%c", item)}' />

    </androidx.constraintlayout.widget.ConstraintLayout>

</layout>
~~~

単純に`MaterialButton`で作ると見た目に変化が無くて混乱しますから、`TextView`で索引の文字を表示させました。でも`TextView`だけだとタップしたときの見た目のフィードバックがないので、同じ位置に重なるように`MaterialButton`も配置します。あと、`MaterialButton`は`android:inset`として上下左右に隙間を持っているので、この隙間をなくすために`0dp`に設定しました。

次に、`IndexAdapter`を作成します。

~~~ kotlin
package com.tail_island.jetbus.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tail_island.jetbus.databinding.ListItemIndexBinding

class IndexAdapter: ListAdapter<Char, IndexAdapter.ViewHolder>(DiffCallback()) {
    lateinit var onIndexClick: (index: Char) -> Unit

    inner class ViewHolder(private val binding: ListItemIndexBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Char) {
            binding.item = item
            binding.executePendingBindings()

            binding.indexButton.setOnClickListener {
                onIndexClick(item)
            }
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<Char>() {
        override fun areItemsTheSame(oldItem: Char, newItem: Char) = oldItem == newItem
        override fun areContentsTheSame(oldItem: Char, newItem: Char) = oldItem == newItem
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder = ViewHolder(ListItemIndexBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false))
    override fun onBindViewHolder(viewHolder: IndexAdapter.ViewHolder, position: Int) = viewHolder.bind(getItem(position))
 }
~~~

うん、見事なまでに`BusStopAdapter`とそっくり。もちろんコピー＆ペーストで作りました。注意すべきは、索引は`Char`一文字で属性がないので、`areItemsTheSame()`と`areContentsTheSame()`の内容が同じである点くらいです。

で、索引はどうしましょ？「あ」から「ん」まで全て並べてもよいのですけど、できるだけ短くしたいので、バス停の一覧に含まれる分だけの頭文字を使うことにしましょう。あと、「か」と「が」が同じになるような考慮もしたい。というわけで、Utility.ktを修正してこんな関数を作成しました。

~~~ kotlin
import com.tail_island.jetbus.model.BusStop

...

private val indexConverter = "ぁぃぅぇぉがぎぐげござじずぜぞだぢっづでどばぱびぴぶぷべぺぼぽゃゅょゎゐゑゔゕゖ".zip("あいうえおかきくけこさしすせそたちつつてとははひひふふへへほほやゆよわいえうかけ").toMap()

fun convertIndex(index: Char): Char {
    return when (index) {
        in '\u30a1'..'\u30fa' -> index - 0x0060
        else                  -> index
    }.let {
        indexConverter[it] ?: it
    }
}

fun getBusStopIndexes(busStops: List<BusStop>): List<Char> {
    return busStops.asSequence().map { busStop -> busStop.phoneticName?.firstOrNull() }.filterNotNull().map { convertIndex(it) }.distinct().toList()
}

fun getBusStopPosition(busStops: List<BusStop>, index: Char): Int {
    return busStops.indexOfFirst { busStop -> busStop.phoneticName?.firstOrNull()?.let { convertIndex(it) == index } ?: false }
}
~~~

`indexConverter`という`Map`を作成して、「ぁ」を「あ」に変換できるようにします。全部の文字をこの`Map`で変換するのは記述が面倒だったので、`convertIndex()`関数を作成して、変換が不要な場合はそのまま、そうでなければ`indexConverter`を使用して変換するようにしました。あとは、`List<BusStop>`から関数型プログラミングのテクニックでサクッと索引一覧を作成する`getBusStopIndexes()`関数と、あとで使用する索引から位置を求める`getBusStopPosition()`関数です。

この`getBusStopIndexes()`関数を使用して、`DepartureBusStopViewModel`を完成させます。

~~~ kotlin
package com.tail_island.jetbus.view_model

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.tail_island.jetbus.getBusStopIndexes
import com.tail_island.jetbus.model.Repository

class DepartureBusStopViewModel(private val repository: Repository): ViewModel() {
    val departureBusStops = repository.getObservableBusStops()

    val departureBusStopIndexes = Transformations.map(departureBusStops) {
        getBusStopIndexes(it)
    }
}
~~~

`DepartureBusStopFragment`に新しい`RecyclerView`を追加します。レイアウトのXMLはこんな感じ。

~~~ xml
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    tools:context=".DepartureBusStopFragment">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/recyclerView"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:padding="16dp"
            android:clipToPadding="false"
            app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager" />

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/indexRecyclerView"
            android:layout_width="wrap_content"
            android:layout_height="match_parent"
            android:padding="16dp"
            android:clipToPadding="false"
            app:layout_constraintEnd_toEndOf="parent"
            app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager" />

    </androidx.constraintlayout.widget.ConstraintLayout>

</layout>
~~~

`DepartureBusStopFragment`はこんな感じです。

~~~ kotlin
package com.tail_island.jetbus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSmoothScroller
import com.tail_island.jetbus.adapter.BusStopAdapter
import com.tail_island.jetbus.adapter.IndexAdapter
import com.tail_island.jetbus.databinding.FragmentDepartureBusStopBinding
import com.tail_island.jetbus.view_model.DepartureBusStopViewModel
import javax.inject.Inject

class DepartureBusStopFragment: Fragment() {
    @Inject lateinit var viewModelProviderFactory: AppViewModelProvideFactory

    private val viewModel by viewModels<DepartureBusStopViewModel> { viewModelProviderFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (requireActivity().application as App).component.inject(this)

        return FragmentDepartureBusStopBinding.inflate(inflater, container, false).apply {
            recyclerView.adapter = BusStopAdapter().apply {
                viewModel.departureBusStops.observe(viewLifecycleOwner, Observer {
                    submitList(it)
                })

                onBusStopClick = {
                    findNavController().navigate(DepartureBusStopFragmentDirections.departureBusStopFragmentToArrivalBusStopFragment(it.name))
                }
            }

            indexRecyclerView.adapter = IndexAdapter().apply {
                viewModel.departureBusStopIndexes.observe(viewLifecycleOwner, Observer {
                    submitList(it)
                })

                onIndexClick = {
                    recyclerView.layoutManager!!.startSmoothScroll(
                        AcceleratedSmoothScroller(requireContext()).apply {
                            targetPosition = getBusStopPosition(viewModel.departureBusStops.value!!, it)
                        }
                    )
                }
            }
        }.root
    }
}
~~~

このコードの中の`AcceleratedSmoothScroller`というのは、[RecyclerViewの長距離スムーズスクロールをスムーズにする](https://qiita.com/chibatching/items/52d9b73d244eac52d0d4)を猿真似して作成した良い感じスクロールさせるためのクラスです。`RecylerView`のAPIには、指定した場所にスクロールする機能（画面がいきなり切り替わるので使いづらいユーザー・インターフェースになる）と指定した場所までスムーズにスクロールする機能（使いやすいユーザー・インターフェースになるけど、スクロールが終わるまで長時間かかる）しかなくて、これだけだと指定場所までスクロールするという機能がとても作りづらいんです。この問題は、[RecyclerViewの長距離スムーズスクロールをスムーズにする](https://qiita.com/chibatching/items/52d9b73d244eac52d0d4)ですべて解決ですので、ぜひ読んでください。なお、今回は少しだけ実装を変えたので、クラス名も少しだけ変更しました。

## `BookmarksFragment`と`ArrivalBusStopFragment`を同じやり方で作って、`BusApproachesFragment`を少しだけ修正する

あとは、同じやり方で（コピー＆ペーストをやりまくって）`BookmarksFragment`と`ArrivalBusStopFragment`を作りましょう。少しだけ工夫したのは、list_item_bookmark.xmlの`android:text`で`String.format()`を使用したこと、あと、文字列リソースは`@string/id`と書けば参照できて、それはデータ・バインディングの中でも変わらないことを証明するために`String.format()`の中に`@string/start_to_end_arrow`と書いたことと、ついでだったので文字列をres/values/string.xmlに移動させて、都営バスを思わせる緑色で画面が表示されるようにres/values/colors.xmlを修正したくらい。

で、このままだとブックマークが一つもないので`BookmarksFragment`を正しく実装できたか確認できなかったので、`BusApproachesFragment`にブックマークを追加する機能（`BusApproachesViewModel`の`toggleBookmark()`）を追加しました。

コードの詳細は、GitHubのコードのrecycler-viewブランチをご参照ください。ほとんどコピー＆ペーストなので書くこと何もなかったんですよ……。

ともあれ、大分見た目もしっかりしてきました。

動画

あとは、バスの接近情報を表示するだけ。表示そのものはRecyclerViewでできそうだけど、表示するデータはどうしましょうか？

# [バス接近情報を表示する](https://github.com/tail-island/jetbus/tree/bus-approaches)

バス接近情報を表示する部分は少し複雑なので、データ・アクセス・オブジェクト、`Repository`、`ViewModel`、`Fragment`の順で説明していきます。

## データ・アクセス・オブジェクトを作成する

まずは、バスの接近情報を表示するために必要な情報を取得するデータ・アクセス・オブジェクトを、順を追って作成していきましょう。

### `RouteBusStopPole`

出発バス停名称と到着バス停名称から`Route`を取得するところまでは実装済みですので、`RouteBusStopPole`から考えましょう。`RouteBusStopPole`は`Route`と`BusStopPole`を関連付けるものなのですけど、バスの「接近」情報を表示する本アプリでは、出発バス停以降の`RouteBusStopPole`の情報は不要です（どれだけ遠ざかったかを表示するプログラムなら、出発バス停以降こそが重要なのでしょうけど）。あと、出発バス停よりも遥かに手前の`RouteBusStopPole`も不要です。あと3時間でバスが到着する（バスは出発バス停よりも300手前のバス停を出発した）なんて表示されても困りますもんね。私の独断と偏見で、出発バス停の手前10個までを取得の対象としました。

~~~ kotlin
package com.tail_island.jetbus.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RouteBusStopPoleDao {
    @Insert
    fun add(routeBusStopPole: RouteBusStopPole): Long

    @Query("DELETE FROM RouteBusStopPole")
    fun clear()

    @Query(
        """
            SELECT RouteBusStopPole.*
            FROM RouteBusStopPole
            INNER JOIN (
                SELECT RouteBusStopPole.routeId, RouteBusStopPole.'order'
                FROM RouteBusStopPole
                INNER JOIN BusStopPole ON BusStopPole.id = RouteBusStopPole.busStopPoleId
                WHERE RouteBusStopPole.routeId IN (:routeIds) AND BusStopPole.busStopName = :departureBusStopName
            ) DepartureRouteBusStopPole ON RouteBusStopPole.routeId = DepartureRouteBusStopPole.routeId
            WHERE RouteBusStopPole.'order' <= DepartureRouteBusStopPole.'order' AND RouteBusStopPole.'order' >= DepartureRouteBusStopPole.'order' - 10
        """
    )
    fun getObservablesByRouteIdsAndDepartureBusStopName(routeIds: List<String>, departureBusStopName: String): LiveData<List<RouteBusStopPole>>
}
~~~

うん、副問合せですね。`INNER JOIN`のカッコの中で、`Route`と出発バス停名称に関連付けられたRouteBusStopPoleを取得します。で、外側のSQLの`WHERE`で、出発の`RouteBusStopPole`よりも`order`が小さく、かつ、出発の`RouteBusStopPole`の`order`-10よりも大きいとやることで、先に述べた条件を満たす`RouteBusStopPole`を取得しているというわけ。

### `BusStopPole`

バス停の情報を表示できるように、`BusStopPole`も取得しましょう。`RouteBusStop`を取得済みなので、これはとても簡単です。

~~~ kotlin
package com.tail_island.jetbus.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BusStopPoleDao {
    ...

    @Query("SELECT * FROM BusStopPole WHERE id IN (:ids)")
    fun getObservablesByIds(ids: List<String>): LiveData<List<BusStopPole>>
}
~~~

取得した`RouteBusStopPole`の`busStopPoleId`のリストを引数に渡すわけですな。

### `TimeTable`

前にも述べましたが、バスは一つの路線を日に何回も行き来していますから、`Route`と`TimeTable`の関係は1対多となっています。よって、複数ある`TimeTable`の中から一つを選ばなければなりません。道路が混んでいるときと空いているときは時刻表が変わるかもしれませんから、到着バス停に関連付けられた`TimeTableDetail.arrival`が現在時刻に最も近いものを選ぶのが良いでしょう。ということで、`TimeTableDao`に以下の`@Query`を追加しました。

~~~ kotlin
package com.tail_island.jetbus.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TimeTableDao {
    ...

    @Query(
        """
            SELECT TimeTable.*
            FROM TimeTable
            INNER JOIN (
                SELECT TimeTable.routeId, MIN(TimeTable.id) AS id
                FROM TimeTable
                INNER JOIN TimeTableDetail ON TimeTableDetail.timeTableId = TimeTable.id
                INNER JOIN BusStopPole ON BusStopPole.id = TimeTableDetail.busStopPoleId
                WHERE
                    TimeTable.routeId IN (:routeIds) AND
                    BusStopPole.busStopName = :departureBusStopName AND
                    NOT EXISTS (
                        SELECT T1.*
                        FROM TimeTable AS T1
                        INNER JOIN TimeTableDetail AS T2 ON T2.timeTableId = T1.id
                        INNER JOIN BusStopPole AS T3 ON T3.id = T2.busStopPoleId
                        WHERE T1.routeId = TimeTable.routeId AND T3.busStopName = BusStopPole.busStopName AND ABS(T2.arrival - :now) < ABS(TimeTableDetail.arrival - :now)
                    )
                GROUP BY TimeTable.routeId
            ) AS T ON T.id = TimeTable.id
        """)
    fun getObservablesByRouteIdsAndDepartureBusStopName(routeIds: List<String>, departureBusStopName: String, now: Int): LiveData<List<TimeTable>>
}
~~~

ある特定の条件下での集約や分析を実現するSQLのwindow関数を使えば簡単に作れそうなのですけど、残念なことにSQLite3がwindow関数をサポートしたのはバージョン3.25.0で、2020年3月現在で最新のAndroid 10であってもSQLite3のバージョンは3.22.0です……。だから、上のSQLでは`NOT EXISTS`の中の副問合せで代用しました。副問合せの外側の行よりも到着バス停に関連付けられた`TimeTableDetail.arrival`から現在時刻を引いた結果の絶対値が小さいものが存在しない（`NOT EXISTS`）なので、結果として到着バス停に関連付けられた`TimeTableDetail.arrival`が現在時刻に最も近いものがSELECTされます。……現在時刻との差が同じものが複数ある場合に、全部取得されちゃうんだけどな。なので、`INNER JOIN`でもう一回副問合せして、`GROUP BY`して最小（順序が付いて一つに絞れるなら何でもいいので、最大とかでも大丈夫）の`TimeTable.id`と`INNER JOIN`しています。ちょっと（かなり）複雑なSQL文ですけど、でもたぶん、普通のプログラミング言語でループを回してデータを取得する処理を書くよりは楽だと思いますよ。O/Rマッピング・ツールがなかった頃にプログラムを書いていた私のようなおっさんに頼めば、この程度のSQLは大喜びで一瞬で書いてくれるはずです。おっさんとハサミは使いようですよ。職場のおっさんを大事にしましょう。

### `TimeTableDetail`

`TimeTable`を取得できたので、`TimeTableDetail`も取得します。`RouteBusStopPole`のときと同じで、出発バス停以降の`TimeTableDetail`も、出発バス停のはるか手前の`TimeTableDetail`も不要……なのですけど、同じ条件式を2回書くのは大変なので、取得済みの`BusStopPole`を活用することにしましょう。

~~~ kotlin
package com.tail_island.jetbus.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TimeTableDetailDao {
    ...

    @Query("SELECT * FROM TimeTableDetail WHERE timeTableId IN (:timeTableIds) AND busStopPoleId IN (:busStopPoleIds) ORDER BY 'order'")
    fun getObservablesByTimeTableIdsAndBusStopPoleIds(timeTableIds: List<String>, busStopPoleIds: List<String>): LiveData<List<TimeTableDetail>>
}
~~~

うん、SQL便利。これでデータ・アクセス・オブジェクトの作成は終了です。

## `Repository`を作成する

`Repository`でデータベースとWebサービスの差異を吸収して統一した操作を実現する……のが理想だけど、実際はまず無理！　トランザクションがある環境とない環境で同じ操作なんて無理なんだよ（と私は思います）！　とはいえ、`Repository`というレイヤーはやはり便利です。以前作成した`syncDatabase()`メソッドのようなデータベースとWebサービスの複雑な呼び出しを隠蔽するメソッドを置けますし、データ・アクセス・オブジェクトのメソッドがへっぽこだったりWebサービスの仕様が物足りなかったりする場合を補う処理を書けますから。

というわけで、作成した`Repository`は以下の通り。

~~~ kotlin
package com.tail_island.jetbus.model

import android.util.Log
import androidx.room.withTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.Call
import java.io.IOException
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val database: AppDatabase, private val webService: WebService, @param:Named("consumerKey") private val consumerKey: String) {
    ...

    suspend fun syncTimeTables(routes: Iterable<Route>) = withContext(Dispatchers.IO) {
        try {
            for (route in routes) {
                delay(0)  // 一応だけど、キャンセル可能にしてみました。。。

                if (database.getTimeTableDao().getCountByRouteId(route.id) > 0) {
                    continue
                }

                val timeTableJsonArray = getWebServiceResultBody { webService.busTimeTable(consumerKey, route.id) } ?: return@withContext null

                database.withTransaction {
                    for (timeTableJsonObject in timeTableJsonArray.map { it.asJsonObject }) {
                        val timeTable = TimeTable(
                            timeTableJsonObject.get("owl:sameAs").asString,
                            timeTableJsonObject.get("odpt:busroutePattern").asString
                        ).also {
                            database.getTimeTableDao().add(it)
                        }

                        for (timeTableDetailJsonObject in timeTableJsonObject.get("odpt:busTimetableObject").asJsonArray.map { it.asJsonObject }) {
                            TimeTableDetail(
                                timeTable.id,
                                timeTableDetailJsonObject.get("odpt:index").asInt,
                                timeTableDetailJsonObject.get("odpt:busstopPole").asString,
                                timeTableDetailJsonObject.get("odpt:arrivalTime").asString.split(":").let { (hour, minute) -> hour.toInt() * 60 * 60 + minute.toInt() * 60 }
                            ).also {
                                it.id = database.getTimeTableDetailDao().add(it)
                            }
                        }
                    }
                }
            }

            Unit

        } catch (e: IOException) {
            Log.e("Repository", "${e.message}")
            null
        }
    }

    suspend fun clearBookmarks() = withContext(Dispatchers.IO) {
        database.getBookmarkDao().clear()
    }

    suspend fun toggleBookmark(departureBusStopName: String, arrivalBusStopName: String) = withContext(Dispatchers.IO) {
        val bookmark = database.getBookmarkDao().get(departureBusStopName, arrivalBusStopName)

        if (bookmark == null) {
            database.getBookmarkDao().add(Bookmark(departureBusStopName, arrivalBusStopName))
        } else {
            database.getBookmarkDao().remove(bookmark)
        }
    }

    suspend fun getBuses(routes: Iterable<Route>, routeBusStopPoles: Iterable<RouteBusStopPole>) = withContext(Dispatchers.IO) {
        try {
            val busStopPoleIds = routeBusStopPoles.groupBy { it.routeId }.map { (routeId, routeBusStopPoles) -> Pair(routeId, routeBusStopPoles.map { it.busStopPoleId }.toSet()) }.toMap()

            getWebServiceResultBody { webService.bus(consumerKey, routes.map { it.id }.joinToString(",")) }?.filter { bus ->
                // routeBusStopPolesに含まれるバス停を出発したところ、かつ、routeBusStopPolesの同じルートの最後（つまり出発バス停）を出発したのではない
                bus.fromBusStopPoleId in busStopPoleIds.getValue(bus.routeId) && bus.fromBusStopPoleId != routeBusStopPoles.filter { it.routeId == bus.routeId }.sortedByDescending { it.order }.first().busStopPoleId
            }

        } catch (e: IOException) {
            Log.e("Repository", "${e.message}")
            null
        }
    }

    ...
    fun getObservableBusStopPolesByRouteBusStopPoles(routeBusStopPoles: Iterable<RouteBusStopPole>) = database.getBusStopPoleDao().getObservablesByIds(routeBusStopPoles.map { it.busStopPoleId }.distinct())  // 複数の路線が同じバス停を含んでいる可能性があるので、distinct()しておきます。
    ...
    fun getObservableRouteBusStopPolesByRoutes(routes: Iterable<Route>, departureBusStopName: String) = database.getRouteBusStopPoleDao().getObservablesByRouteIdsAndDepartureBusStopName(routes.map { it.id }, departureBusStopName)
    fun getObservableRoutesByDepartureBusStopNameAndArrivalBusStopName(departureBusStopName: String, arrivalBusStopName: String) = database.getRouteDao().getObservablesByDepartureBusStopNameAndArrivalBusStopName(departureBusStopName, arrivalBusStopName)
    fun getObservableTimeTablesByRoutesAndDepartureBusStop(routes: Iterable<Route>, departureBusStopName: String) = database.getTimeTableDao().getObservablesByRouteIdsAndDepartureBusStopName(routes.map { it.id }, departureBusStopName, Calendar.getInstance().let { it.get(Calendar.HOUR_OF_DAY) * 60 * 60 + it.get(Calendar.MINUTE) * 60 })
    fun getObservableTimeTableDetailsByTimeTablesAndBusStopPoles(timeTables: Iterable<TimeTable>, busStopPoles: Iterable<BusStopPole>) = database.getTimeTableDetailDao().getObservablesByTimeTableIdsAndBusStopPoleIds(timeTables.map { it.id }, busStopPoles.map { it.id })
}
~~~

`syncTimeTables()`は`syncDatabase()`の外側にループがもう一つ付いただけ。これで、`TimeTable`と`TimeTableDetail`をデータベースにキャッシュできるようになりました。`clearBookmarks()`はバス停が廃止された場合にブックマークの削除ができなくなっちゃうじゃんと考えてこっそり追加した[ブックマークを全て削除]メニュー向けのメソッド、`toggleBookmark()`は前の章で作成した（けど説明をし忘れた）ブックマークを設定したり解除したりするためのメソッドです。

重要なのはここから。`getBuses()`はWebサービスを呼び出しているのですけど、このWebサービスって`Route`のバスを全て返すんですよ。で、データ・アクセス・オブジェクトのところで何度も考えたように、出発バス停以降を走っているバスや出発バス停のはるか手前を走っているバスの情報は不要。なので、不要な情報を削除する処理をこのメソッドの中に入れています。出発バス停を出発したバスを除外するところが格好悪いコードになっていますけど、ご容赦ください。

あと、たとえば`BusStopPoleDao.getObservablesByIds()`ではIDの集合が引数になっていたわけですけど、それだと呼び出しづらいので、`BusStopPoleDao.getObservablesByIds()`をラップする`getObservableBusStopPolesByRouteBusStopPoles()`は`RouteBusStopPole`の集合を引数に呼び出せるようにしたりしています。`getObservableRouteBusStopPolesByRoutes()`や`getObservableTimeTableDetailsByTimeTablesAndBusStopPoles()`も同様。`getObservableTimeTablesByRoutesAndDepartureBusStop()`では、それにくわええて現在時刻の引数を生成しています。

うん、`Repository`を作っておいて良かった。これなら`ViewModel`の作成は簡単でしょう。

## ViewModelを作成する

たしかに`ViewModel`の作成は簡単っちゃあ簡単なんですけど、面倒くださいです。その理由は`MediatorLiveData`。同じような記述を何度も繰り返さなければならないので、簡単だけど面倒でした……。その`BusApproachesViewModel`はこんな感じ。

~~~ kotlin
package com.tail_island.jetbus.view_model

import androidx.lifecycle.*
import com.tail_island.jetbus.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BusApproachesViewModel(private val repository: Repository): ViewModel() {
    val departureBusStopName = MutableLiveData<String>()

    val arrivalBusStopName = MutableLiveData<String>()

    val bookmark = MediatorLiveData<Bookmark?>().apply {
        var source: LiveData<Bookmark?>? = null

        fun update() {
            val departureBusStopNameValue = departureBusStopName.value ?: return
            val arrivalBusStopNameValue   = arrivalBusStopName.value   ?: return

            source?.let {
                removeSource(it)
            }

            source = repository.getObservableBookmarkByDepartureBusStopNameAndArrivalBusStopName(departureBusStopNameValue, arrivalBusStopNameValue).also {
                addSource(it) { sourceValue ->
                    value = sourceValue
                }
            }
        }

        addSource(departureBusStopName) { update() }
        addSource(arrivalBusStopName)   { update() }
    }

    val routes = MediatorLiveData<List<Route>>().apply {
        var source: LiveData<List<Route>>? = null

        fun update() {
            val departureBusStopNameValue = departureBusStopName.value ?: return
            val arrivalBusStopNameValue   = arrivalBusStopName.value   ?: return

            source?.let {
                removeSource(it)
            }

            source = repository.getObservableRoutesByDepartureBusStopNameAndArrivalBusStopName(departureBusStopNameValue, arrivalBusStopNameValue).also {
                addSource(it) { sourceValue ->
                    value = sourceValue
                }
            }
        }

        addSource(departureBusStopName) { update() }
        addSource(arrivalBusStopName)   { update() }
    }

    val routeBusStopPoles = MediatorLiveData<List<RouteBusStopPole>>().apply {
        var source: LiveData<List<RouteBusStopPole>>? = null

        fun update() {
            val routesValue               = routes.value               ?: return
            val departureBusStopNameValue = departureBusStopName.value ?: return

            source?.let {
                removeSource(it)
            }

            source = repository.getObservableRouteBusStopPolesByRoutes(routesValue, departureBusStopNameValue).also {
                addSource(it) { sourceValue ->
                    value = sourceValue
                }
            }
        }

        addSource(routes)               { update() }
        addSource(departureBusStopName) { update() }
    }

    val busStopPoles = Transformations.switchMap(routeBusStopPoles) { routeStopPolesValue ->
        repository.getObservableBusStopPolesByRouteBusStopPoles(routeStopPolesValue)
    }

    val timeTables = MediatorLiveData<List<TimeTable>>().apply {
        var source: LiveData<List<TimeTable>>? = null
        var job: Job? = null

        fun update() = viewModelScope.launch {  // Jobの管理をしたいので、launchします
            val routesValue               = routes.value               ?: return@launch
            val departureBusStopNameValue = departureBusStopName.value ?: return@launch

            source?.let {
                removeSource(it)
            }

            source = repository.getObservableTimeTablesByRoutesAndDepartureBusStop(routesValue, departureBusStopNameValue).also {
                addSource(it) { sourceValue ->
                    value = sourceValue
                }
            }

            job?.cancelAndJoin()

            job = viewModelScope.launch {
                repository.syncTimeTables(routesValue)
            }
        }

        addSource(routes)               { update() }
        addSource(departureBusStopName) { update() }
    }

    val timeTableDetails = MediatorLiveData<List<TimeTableDetail>>().apply {
        var source: LiveData<List<TimeTableDetail>>? = null

        fun update() {
            val timeTablesValue   = timeTables.value   ?: return
            val busStopPolesValue = busStopPoles.value ?: return

            source?.let {
                removeSource(it)
            }

            source = repository.getObservableTimeTableDetailsByTimeTablesAndBusStopPoles(timeTablesValue, busStopPolesValue).also {
                addSource(it) { sourceValue ->
                    value = sourceValue
                }
            }
        }

        addSource(timeTables)   { update() }
        addSource(busStopPoles) { update() }
    }

    val buses = MediatorLiveData<List<Bus>>().apply {
        var job: Job? = null

        fun update() = viewModelScope.launch {  // Jobの管理をしたいので、launchします
            val routesValue            = routes.value            ?: return@launch
            val routeBusStopPolesValue = routeBusStopPoles.value ?: return@launch

            job?.cancelAndJoin()

            job = viewModelScope.launch {
                while (true) {
                    value = repository.getBuses(routesValue, routeBusStopPolesValue) ?: listOf()

                    delay(15000)
                }
            }
        }

        addSource(routes)            { update() }
        addSource(routeBusStopPoles) { update() }
    }

    val busApproaches = MediatorLiveData<List<BusApproach>>().apply {
        fun update() {
            val routesValue            = routes.value            ?: return
            val routeBusStopPolesValue = routeBusStopPoles.value ?: return
            val busStopPolesValue      = busStopPoles.value      ?: return
            val timeTablesValue        = timeTables.value        // syncTimeTable()は時間がかかるので、
            val timeTableDetailsValue  = timeTableDetails.value  // 時刻表がない場合はバス停の数で代用することにしてnullでも強行します。
            val busesValue             = buses.value             ?: return

            value = busesValue.map { bus ->
                BusApproach(
                    bus.id,
                    timeTablesValue?.find { timeTable -> timeTable.routeId == bus.routeId }?.let { timeTable ->
                        timeTableDetailsValue?.filter { it.timeTableId == timeTable.id }?.sortedByDescending { it.order }?.takeWhile { it.busStopPoleId != bus.fromBusStopPoleId }?.zipWithNext()?.map { (next, prev) -> next.arrival - prev.arrival }?.sum()
                    },
                    routeBusStopPolesValue.sortedByDescending { it.order }.let { it.first().order - it.find { routeBusStopPole -> routeBusStopPole.busStopPoleId == bus.fromBusStopPoleId }!!.order },
                    routesValue.find { it.id == bus.routeId }!!.name,
                    busStopPolesValue.find { it.id == bus.fromBusStopPoleId }!!.busStopName
                )
            }.sortedWith(compareBy({ it.willArriveAfter ?: Int.MAX_VALUE }, { it.busStopCount }))
        }

        addSource(routes)            { update() }
        addSource(routeBusStopPoles) { update() }
        addSource(busStopPoles)      { update() }
        addSource(timeTables)        { update() }
        addSource(timeTableDetails)  { update() }
        addSource(buses)             { update() }
    }

    fun toggleBookmark() {
        viewModelScope.launch {
            val departureBusStopNameValue = departureBusStopName.value ?: return@launch
            val arrivalBusStopNameValue   = arrivalBusStopName.value   ?: return@launch

            repository.toggleBookmark(departureBusStopNameValue, arrivalBusStopNameValue)
        }
    }
}
~~~

うん、本当に面倒くさかった……。

でも、`departureBusStopName`と`arrivalBusStopName`、`bookmark`、`routes`、`routeBusStopPoles`、`busStopPoles`、`timeTableDetails`については、これまでに説明したやり方をそのまま繰り返しただけなので簡単です。

問題は`timeTables`プロパティと`buses`プロパティ、`busApproaches`プロパティです。`Repository`の`syncTimeTables()`メソッドを実行しないとデータベースは空なので、だからどこかで`syncTimeTables()`を呼ばなければ`timeTables`はいつまでも空集合を返します。`buses`はWebサービスから取得する値で、変更通知が来ませんから自分で値を定期的に更新しなければなりません。バスの接近情報そのものである`busApproaches`は、全ての情報を手作りしなければなりません……。

というわけで、まずは`timeTables`プロパティから。`syncTimeTables()`を呼ぶのに一番良いタイミングは`routes`プロパティと`departureBusStopName`プロパティの両方に値が設定された時なので、`MediatorLiveData<List<TimeTable>>().apply { ... }`の中に`syncTimeTables()`を呼び出す処理を入れたい。値を取得する処理の中に更新処理を入れるのは目的違いな気もするけど、他に適当な場所が無いから我慢。で、`syncTimeTables()`はコルーチンで呼び出さなければならないので`viewModelScope.launch { ... }`して呼び出します。今回は`routes`も`departureBusStopName`も変更がないので実は考慮しなくても動作するのですけど、念の為に、`syncTimeTables()`している最中にもう一度`viewModelScope.launch { ... }`が動いてしまっても二重に処理されないための考慮をしておきたい。というわけで、`source`と同様に`job`という変数を作成して、`cancelAndJoin()`するようにしました。`cancelAndJoin()`も`suspend`なメソッドなので、`update()`全体を`viewModelScope.launch { ... }`で囲んでいます。これで`timeTables`プロパティは完成。`syncTimeTables()`が終わればデータベースが更新されたことがLiveDataで通知されて、結果として`timeTables`プロパティに値が設定されます。

`buses`プロパティも同じやり方で作りました。こちらは15,000ミリ秒の間隔で繰り返される無限ループです。コルーチンがあると気軽に無限ループが使えて便利ですな。

最後の`busApproaches`プロパティはゴリゴリにロジックを書いて実現します。で、説明を忘れていたのですけど、このプロパティを作成する前にバスの接近情報を表現する`BusApproach`を追加しておきます。

~~~ kotlin
package com.tail_island.jetbus.model

data class BusApproach(
    val id: String,
    val willArriveAfter: Int?,
    val busStopCount: Int,
    val routeName: String,
    val leftBusStopName: String
)
~~~

何秒後に到着するのかを表現する`willArriveAfter`プロパティに加えて`busStopCount`プロパティがあるのは、`TimeTable`を同期する`syncTimeTables()`はかなり時間がかかるので、それまでの間はあといくつバス停があるのかで到着時刻を判断していただくためです。と、こんな感じに`TimeTable`がまだない場合も想定しているので、`busApproaches`プロパティの中の`timeTablesValue`と`timeTableDetailsValue`を設定する部分では、`null`の場合にリターンする処理が省かれているわけですな。

さて、`busApproaches`プロパティでは、`buses`の要素である`Bus`単位で`BusApproach`を作成します。関数型プログラミングの`map`ですね。`willArriveAfter`は、TimeTableDetail（出発バス停から手前10バス停分が入っている）を`sortedByDescending()`で逆順にソートして、`takeWhile`で`bus.fromBusStopPoleId`と異なる間だけ取得します。これで、出発バス停からバスが最後に出発したバス停の次のバス停までを入手できるというわけ。あとは、`zipWithNext()`でペアを作成して、時刻の差を求めて、合計する。これで、何秒後に到着するのかの情報を取得できました！　関数型プログラミングは本当に楽ちんですな。

## Fragmentを作成する

もう終わったも同然。機械作業でRecyclerViewを組み込みます。

まずはlist_item_bus_approaches.xml。

~~~ xml
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <data>
        <variable name="item" type="com.tail_island.jetbus.model.BusApproach" />
    </data>

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <ImageView
            android:id="@+id/imageView"
            android:layout_width="32dp"
            android:layout_height="32dp"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="@id/routeNameTextView"
            app:layout_constraintBottom_toBottomOf="@id/leftBusStopName"
            android:src="@drawable/ic_bus" />

        <TextView
            android:id="@+id/routeNameTextView"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_marginStart="16dp"
            android:layout_marginEnd="16dp"
            app:layout_constraintStart_toEndOf="@id/imageView"
            app:layout_constraintEnd_toStartOf="@id/willArriveAfterTextView"
            app:layout_constraintTop_toTopOf="parent"
            android:text="@{item.routeName}" />

        <TextView
            android:id="@+id/leftBusStopNameTextView"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            app:layout_constraintStart_toStartOf="@id/routeNameTextView"
            app:layout_constraintEnd_toEndOf="@id/routeNameTextView"
            app:layout_constraintTop_toBottomOf="@id/routeNameTextView"
            android:text='@{String.format("%sを通過", item.leftBusStopName)}' />

        <TextView
            android:id="@+id/willArriveAfterTextView"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toTopOf="@id/routeNameTextView"
            app:layout_constraintBottom_toBottomOf="@id/leftBusStopName"
            android:textColor="@color/colorAccent"
            android:textSize="28sp"
            android:text='@{item.willArriveAfter == null ? "-" : String.format("%d 分", safeUnbox(item.willArriveAfter) / 60)}' />

        <View
            android:layout_width="match_parent"
            android:layout_height="0dp"
            android:layout_marginTop="16dp"
            app:layout_constraintTop_toBottomOf="@id/leftBusStopName" />

    </androidx.constraintlayout.widget.ConstraintLayout>

</layout>
~~~

少しだけ見た目にこだわってみました。Google様が用意してくださっているアイコンの中からバスのアイコンを探し出して`ic_bus`を登録して`<ImageView>`タグで表示しています。`routeNameTextView`で路線名を、`leftBusStopNameTextView`でどのバス停を出たところなのか、`willArriveAfterTextView`であと何分で到着するのかを表示します。あ、最後に追加してある`<View>`タグは、強制的に隙間を作るためのものです……。

`BusApproachAdapter`はいつもどおりのコピー＆ペースト。

~~~ kotlin
package com.tail_island.jetbus.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tail_island.jetbus.databinding.ListItemBusApproachBinding
import com.tail_island.jetbus.model.BusApproach

class BusApproachAdapter: ListAdapter<BusApproach, BusApproachAdapter.ViewHolder>(DiffCallback()) {
    inner class ViewHolder(private val binding: ListItemBusApproachBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BusApproach) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<BusApproach>() {
        override fun areItemsTheSame(oldItem: BusApproach, newItem: BusApproach) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: BusApproach, newItem: BusApproach) = oldItem == newItem
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder = ViewHolder(ListItemBusApproachBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false))
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(getItem(position))
}
~~~

fragment_bus_approaches.xmlに`RecyclerView`を追加します。

~~~ xml
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    tools:context=".DepartureBusStopFragment">

    <data>
        <variable name="viewModel" type="com.tail_island.jetbus.view_model.BusApproachesViewModel" />
    </data>

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <TextView
            android:id="@+id/departureBusStopAndArrivalBusStopTextView"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:layout_marginStart="16dp"
            android:layout_marginEnd="16dp"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            android:text='@{String.format("%s %s %s", viewModel.departureBusStopName, @string/start_to_end_arrow, viewModel.arrivalBusStopName)}' />

        <ImageView
            android:id="@+id/bookmarkImageView"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginEnd="16dp"
            app:layout_constraintTop_toTopOf="@id/departureBusStopAndArrivalBusStopTextView"
            app:layout_constraintBottom_toBottomOf="@id/departureBusStopAndArrivalBusStopTextView"
            app:layout_constraintEnd_toEndOf="parent"
            android:src='@{viewModel.bookmark != null ? @drawable/ic_bookmark_on : @drawable/ic_bookmark_off}' />

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/recyclerView"
            android:layout_width="match_parent"
            android:layout_height="0dp"
            android:paddingBottom="16dp"
            android:paddingStart="16dp"
            android:paddingEnd="16dp"
            android:clipToPadding="false"
            android:layout_marginTop="16dp"
            app:layout_constraintTop_toBottomOf="@id/departureBusStopAndArrivalBusStopTextView"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager" />

        <TextView
            android:id="@+id/noBusApproachesTextView"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            app:layout_constraintStart_toStartOf="@id/departureBusStopAndArrivalBusStopTextView"
            app:layout_constraintTop_toBottomOf="@id/departureBusStopAndArrivalBusStopTextView"
            android:visibility="gone"
            android:text="@string/no_bus_approaches" />

    </androidx.constraintlayout.widget.ConstraintLayout>

</layout>
~~~

データ件数が0件だった場合に画面に何も表示されないと混乱するので、`noBusApproachesTextView`で「接近中のバスはありません」と表示するようにしています。

これで最後の`BusApproachesFragment`は、処理の大部分を他のレイヤに移動させているのでとても簡単です。

~~~ kotlin
package com.tail_island.jetbus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.tail_island.jetbus.adapter.BusApproachAdapter
import com.tail_island.jetbus.databinding.FragmentBusApproachesBinding
import com.tail_island.jetbus.view_model.BusApproachesViewModel
import javax.inject.Inject

class BusApproachesFragment: Fragment() {
    @Inject lateinit var viewModelProviderFactory: AppViewModelProvideFactory

    private val viewModel by viewModels<BusApproachesViewModel> { viewModelProviderFactory }

    private val args by navArgs<BusApproachesFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (requireActivity().application as App).component.inject(this)

        viewModel.departureBusStopName.value = args.departureBusStopName
        viewModel.arrivalBusStopName.value   = args.arrivalBusStopName

        return FragmentBusApproachesBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel      = this@BusApproachesFragment.viewModel

            bookmarkImageView.setOnClickListener {
                this@BusApproachesFragment.viewModel.toggleBookmark()
            }

            recyclerView.adapter = BusApproachAdapter().apply {
                this@BusApproachesFragment.viewModel.busApproaches.observe(viewLifecycleOwner, Observer {
                    noBusApproachesTextView.visibility = if (it.isEmpty()) { View.VISIBLE } else { View.GONE }

                    submitList(it)
                })
            }
        }.root
    }
}
~~~

これだけで、はい。動きました！

動画

これで通勤が楽になるのでもう少しサラリーマンを続けられそうです。素晴らしい！

# [Image Asset Studioでアイコンを作れば、完成！](https://github.com/tail-island/jetbus/tree/master)

でも、まだ不十分。アイコンを作らないとね。Android Studioのメニューから[File] - [New] - [Image Asset]メニューを選んで、アプリのアイコンを作成しましょう。

私は絵心がないので、Android StudioのClip Artを流用して作るんだけどな。[Source Asset]の[Asset Type]の[Clip Art]ラジオ・ボタンをクリックすると、Android Studioが提供するアイコンから選べるようになります。

図

Clip Artをクリックして、バスのアイコンをクリックします。

図

色を設定します。今回は真っ白にしたいので、「FFFFFF」を入力します。

図

背景の絵を考えるのも面倒でしたので、単色でやりましょう。[Background Layer]タブを選択して、Colorをクリックします。背景色は、アプリの`colorPrimaryDark`に設定した色の「006428」です。この色は[Color Tool](https://material.io/resources/color/#!/?view.left=0&view.right=1)で作成しました。Primary Colorに適当な色を設定するだけでPrimary Dark Colorが作成されてとても便利ですよ。

図

あとは、[Next]ボタンをクリックして[Finish]ボタンをクリックして、ビルドしてインストールして、でも何故かアプリのアイコンが変わりませんでした……。

その理由は、drawableのリソースのXMLにはAndroidのバージョンで記述できる事柄に差異があって、Android Studioがデフォルトで作成したアプリのアイコンは新しいバージョン向け、Image Assetで先程作成したアプリのアイコンは古いバージョン向けのファイルを作成していたため。というわけで、不要になった以前のdrawableを消しちゃいましょう。app/src/main/res/drawable-v24フォルダをまるっと削除しました。

まだ終わりません。今回は単色の背景にしたので、app/src/main/res/valuesフォルダの中にic_launcher_background.xmlが生成されています。でも、以前のアイコンはいろいろ描いてあるベクトル・グラフィックスだったので、app/src/main/res/drawablesにあったんですよ。以前のアイコン向けの背景が悪さをするのを防ぐために、app/src/main/res/drawable/ic_launcher_background.xmlも削除しました。

以上！　これで本当の本当に完了です。いろいろあったけど、Androidアプリの開発って別に難しくない、というか楽チンで美味しいでしょ？

図

図
