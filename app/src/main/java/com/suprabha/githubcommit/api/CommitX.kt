package com.suprabha.githubcommit.api

data class CommitX(
    val author: AuthorXXX,
    val comment_count: Int,
    val committer: CommitterXX,
    val message: String,
    val tree: TreeX,
    val url: String,
    val verification: VerificationX
)