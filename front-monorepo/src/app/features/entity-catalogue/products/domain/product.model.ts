export interface Product {
  id: string;
  name: string;
  description: string;
  price: number;
  active: boolean;
  moodleSyncEnabled: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface ProductPayload {
  name: string;
  description: string;
  price: number;
  moodleSyncEnabled: boolean;
}