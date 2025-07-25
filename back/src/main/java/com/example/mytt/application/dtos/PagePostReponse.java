package com.example.mytt.application.dtos;

import java.util.List;

public record PagePostReponse(int paginaAtual, int totalPaginas, List<PostResponse> posts) {}
