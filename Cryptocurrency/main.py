# install: pip install cosmospy

from cosmospy import BIP32DerivationError, seed_to_privkey, privkey_to_pubkey, pubkey_to_address, privkey_to_address
from cosmospy.typing import Wallet
import mnemonic


def m_generate_wallet(*, path: str = "m/44'/118'/0'/0/0", hrp: str = "cosmos") -> Wallet:
    # 12字註記詞設定128，24字設定256
    while True:
        phrase = mnemonic.Mnemonic(language="english").generate(strength=128)
        try:
            privkey = seed_to_privkey(phrase, path=path)
            break
        except BIP32DerivationError:
            pass
    pubkey = privkey_to_pubkey(privkey)
    address = pubkey_to_address(pubkey, hrp=hrp)
    return {
        "seed": phrase,
        "derivation_path": path,
        "private_key": privkey,
        "public_key": pubkey,
        "address": address,
    }


if __name__ == '__main__':
    wallet = m_generate_wallet()
    print("Mnemonic: ", wallet["seed"])
    print("Public Key: ", wallet["public_key"])
    print("Private Key: ", wallet["private_key"])
    print("Derivation_path: ", wallet["derivation_path"])
    try:
        privkey = seed_to_privkey(wallet["seed"], path="m/44'/118'/0'/0/0")

        print("Cosmos: " + privkey_to_address(privkey, hrp="cosmos"))
        print("Osmo: " + privkey_to_address(privkey, hrp="osmo"))
        print("LikeCoin: " + privkey_to_address(privkey, hrp="like"))

    except BIP32DerivationError:
        print("No valid private key in this derivation path!")
