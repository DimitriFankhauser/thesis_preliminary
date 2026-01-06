# credit to: https://dev.to/bansikah/deploying-an-aws-ec2-instance-with-terraform-and-ssh-access-d09
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.0"
    }
    tls = {
      source  = "hashicorp/tls"
      version = "~> 4.0"
    }
  }
  required_version = ">= 1.2.0"
}

provider "aws" {
  region = var.aws_region
}
# Generate an RSA private key
resource "tls_private_key" "tf-keypair" {
  algorithm = "RSA"
  rsa_bits  = 2048
}

# Save the private key to a local file
resource "local_file" "private_key" {
  content  = tls_private_key.tf-keypair.private_key_pem
  filename = "${path.root}/tf-key-pair.pem"
}

# Create a security group that allows SSH
resource "aws_security_group" "ssh_access" {
  name        = "allow_ssh"
  description = "Allow SSH inbound traffic"

  ingress {
    description = "SSH from anywhere"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]  # WARNING: This allows SSH from anywhere
  }

  egress {
    description = "Allow all outbound traffic"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "allow_ssh"
  }
}

# Create an AWS Key Pair using the generated public key
resource "aws_key_pair" "tf-keypair" {
  key_name   = "tf-key-pair"
  public_key = tls_private_key.tf-keypair.public_key_openssh
}


# Update your EC2 instance to use this security group
resource "aws_instance" "demo-instance" {
  ami                    = var.aws_ami_image
  instance_type          = var.aws_instance_type
  key_name               = aws_key_pair.tf-keypair.key_name
  vpc_security_group_ids = [aws_security_group.ssh_access.id]  # Add this line

  tags = {
    Name = "ITIA-Practice"
  }
}

