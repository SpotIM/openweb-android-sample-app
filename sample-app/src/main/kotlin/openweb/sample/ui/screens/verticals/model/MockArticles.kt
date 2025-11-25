package openweb.sample.ui.screens.verticals.model

@Suppress("StringLiteralDuplication")
object MockArticles {
    fun news() = ArticleData(
        conversationIds = ConversationIdentifiers(
            spotId = "sp_LmNIcv7z",
            postId = "news_1"
        ),
        title = "Government officials announce sweeping reforms that could reshape the economic landscape",
        paragraphs = listOf(
            """
                Government officials announce sweeping reforms that could reshape the economic landscape. 
                Experts weigh in on potential implications for businesses and consumers alike.
            """.trimIndent(),
            """
                The administration announced today a comprehensive package of economic reforms that analysts say 
                could fundamentally alter the business landscape for years to come.
            """.trimIndent(),
            """
                Speaking at a press conference this morning, officials outlined key provisions including tax 
                incentives for small businesses, streamlined regulatory processes, and new infrastructure 
                investments targeting underserved communities.
            """.trimIndent(),
            """
                "These changes represent the most significant economic policy shift in over a decade," said 
                **Dr. Maria Chen, Chief Economist at the National Economic Council**. "We expect to see immediate 
                impacts on hiring and business formation."
            """.trimIndent(),
            """
                Industry leaders have responded with cautious optimism. The Chamber of Commerce issued a 
                statement praising the regulatory reforms while calling for additional clarity on implementation 
                timelines.
            """.trimIndent(),
            """
                Consumer advocates, meanwhile, have raised questions about potential effects on housing costs 
                and wage growth. Several economists suggest the reforms could accelerate inflation in the short 
                term before stabilizing.
            """.trimIndent(),
            """
                The measures are expected to face debate in Congress next week, with votes anticipated before 
                the end of the month.
            """.trimIndent()
        )
    )

    fun finance() = ArticleData(
        conversationIds = ConversationIdentifiers(
            spotId = "sp_xT4NGStS",
            postId = "finance_1"
        ),
        title = "Apple Stock Surges Amid Strong Q4 Earnings Report.",
        paragraphs = listOf(
            """
               **AAPL** shares jumped **5.2%** in after-hours trading following quarterly results that exceeded analyst
               expectations. Revenue growth driven by **services segment**.
            """.trimIndent(),
            """
               Apple Inc. (AAPL) delivered a standout fourth-quarter performance, with shares surging 5.2% in extended
               trading as the tech giant exceeded Wall Street expectations across nearly every metric.
            """.trimIndent(),
            """
                **Key Highlights:**
                • **Revenue:** $94.8 billion (vs. $89.2B expected)
                • **EPS:** $1.64 (vs. $1.39 expected)
                • **Services Revenue:** $22.3 billion (+18% YoY)
                • **iPhone Revenue:** $45.1 billion (+8% YoY)
            """.trimIndent(),
            """
                The services segment—which includes the App Store, Apple Music, iCloud, 
                and Apple TV+—continued its impressive growth trajectory, 
                now representing nearly 24% of total revenue.
            """.trimIndent(),
            """
                **CEO Tim Cook** highlighted the company's expanding installed base, 
                now exceeding **2 billion active devices** worldwide. 
                "Our ecosystem has never been stronger," Cook said during the earnings call. 
                "Customer engagement and satisfaction remain at all-time highs."
            """.trimIndent(),
            """
                Analysts at **Morgan Stanley** raised their price target to **$215**, 
                citing the recurring revenue stability from services. **Goldman Sachs** maintained a Buy rating, 
                emphasizing the company's **$165 billion cash position** and aggressive share buyback program.
            """.trimIndent(),
            """
                Looking ahead, Apple provided guidance suggesting continued momentum, 
                though management cautioned about potential supply chain constraints affecting 
                the upcoming holiday quarter.
            """.trimIndent()
        )
    )

    fun recipes() = ArticleData(
        conversationIds = ConversationIdentifiers(
            spotId = "sp_LmNIcv7z",
            postId = "recipes_1"
        ),
        title = "The Ultimate Homemade Pasta Recipe Everyone Loves",
        paragraphs = listOf(
            """
                Master the art of fresh pasta with this foolproof recipe. From silky fettuccine to perfect ravioli, 
                learn the techniques that will impress your dinner guests.
            """.trimIndent(),
            """
                There's nothing quite like fresh, homemade pasta. The silky texture and rich flavor simply can't be 
                matched by store-bought varieties. This recipe has been perfected over generations and delivers 
                restaurant-quality results every time.
            """.trimIndent(),
            """
                **Ingredients:** 
                • **2 cups (250g)** all-purpose flour or "00" flour 
                • **3 large eggs**, room temperature 
                • **1 tablespoon** olive oil 
                • **1/2 teaspoon** salt.
            """.trimIndent(),
            """
                **Instructions:**

                **1. Make the dough:** Create a well in the center of your flour on a clean surface. Crack the eggs into the center, add olive oil and salt. Using a fork, gradually incorporate flour from the edges.

                **2. Knead thoroughly:** Once a shaggy dough forms, knead for 8-10 minutes until smooth and elastic. The dough should spring back when poked.

                **3. Rest the dough:** Wrap tightly in plastic and rest at room temperature for 30 minutes. This relaxes the gluten for easier rolling.

                **4. Roll and cut:** Divide dough into 4 portions. Working with one piece at a time (keep others covered), roll to desired thickness—about 1/16" for fettuccine.

                **5. Cook immediately:** Fresh pasta cooks in just 2-3 minutes in salted boiling water. Toss with your favorite sauce and serve.

            """.trimIndent(),
            """
                **Pro Tips:** 
            • **Room temperature eggs** incorporate better 
            • **Don't skip the resting time**—it makes rolling much easier 
            • **Dust with semolina flour** to prevent sticking
            """.trimIndent()
        )
    )

    fun sport() = ArticleData(
        conversationIds = ConversationIdentifiers(
            spotId = "sp_LmNIcv7z",
            postId = "sport_1"
        ),
        title = "Champions League Final: Preview and Predictions",
        paragraphs = emptyList()
    )

    fun video() = ArticleData(
        conversationIds = ConversationIdentifiers(
            spotId = "sp_LmNIcv7z",
            postId = "video_1"
        ),
        title = "Big Buck Bunny - Animated Short Film",
        paragraphs = listOf(
            """
                Watch Big Buck Bunny, an animated short film showcasing beautiful graphics and storytelling.
                Join the conversation and share your thoughts about this classic animation!
            """.trimIndent()
        )
    )

    fun sideRail() = ArticleData(
        conversationIds = ConversationIdentifiers(
            spotId = "sp_LmNIcv7z",
            postId = "siderail_1"
        ),
        title = "The Future of Remote Work: A Deep Dive",
        paragraphs = listOf(
            """
                Remote work has fundamentally transformed how we think about productivity, collaboration, and work-life 
                balance. As we move into a new era, organizations are grappling with how to maintain the benefits 
                while addressing the challenges.
            """.trimIndent(),
            """
                
                **The Shift in Workplace Culture**
                
                The pandemic accelerated a trend that was already underway. Companies that once insisted on in-person 
                presence discovered that many roles could be performed effectively from anywhere. This revelation has 
                sparked a broader conversation about the nature of work itself.
            """.trimIndent(),
            """
                
                **Productivity Findings**
                
                Multiple studies have shown that remote workers often exceed their in-office counterparts in 
                productivity metrics. Without commuting time and with fewer interruptions, many employees report 
                being able to focus more deeply on complex tasks.
            """.trimIndent(),
            """
                
                **The Collaboration Challenge**
                
                However, remote work isn't without its challenges. 
                Spontaneous collaboration—those hallway conversations that 
                spark innovation—becomes harder to replicate in a virtual environment. 
                Companies are experimenting with various solutions, 
                from virtual water coolers to scheduled serendipity sessions.
            """.trimIndent(),
            """
                
                **Mental Health Considerations**
                
                The blurring of boundaries between work and home life has raised concerns about burnout. 
                Setting clear boundaries and maintaining social connections has become crucial 
                for remote workers' wellbeing.
            """.trimIndent(),
            """
                
                **Looking Ahead**
                
                The future likely lies in hybrid models that combine the flexibility of remote work with the 
                collaboration benefits of in-person interaction. 
                Organizations that master this balance will have a significant advantage 
                in attracting and retaining talent.
            """.trimIndent()

        )
    )
}
