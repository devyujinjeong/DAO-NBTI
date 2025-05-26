terraform {
  required_providers {
    null = {
      source = "hashicorp/null"
      version = "3.2.4"
    }
  }
}

# kubectl을 로컬에서 실행하여 YAML 리소스 전체 적용
resource "null_resource" "apply_k8s_yaml" {
  provisioner "local-exec" {
    command = "kubectl apply -f ../k8s/"
  }
}
